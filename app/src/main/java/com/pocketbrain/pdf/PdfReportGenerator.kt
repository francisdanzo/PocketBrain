package com.pocketbrain.pdf

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.pocketbrain.domain.model.*
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object PdfReportGenerator {

    private val fmt = NumberFormat.getCurrencyInstance(Locale.getDefault())
    private val dateFmt = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val nameDateFmt = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    fun generate(
        context: Context,
        transactions: List<Transaction>,
        budget: Budget,
        startMs: Long,
        endMs: Long
    ): File {
        val pageWidth  = 595  // A4 pt
        val pageHeight = 842
        val pdf   = PdfDocument()
        var page  = startPage(pdf, 1, pageWidth, pageHeight)
        var canvas = page.canvas
        var y = 40f

        // ── HEADER ──────────────────────────────────────────────────────────
        val headerPaint = Paint().apply { color = Color.parseColor("#7C3AED"); style = Paint.Style.FILL }
        canvas.drawRect(0f, 0f, pageWidth.toFloat(), 80f, headerPaint)

        val titlePaint = Paint().apply {
            color = Color.WHITE; textSize = 22f; typeface = Typeface.DEFAULT_BOLD
        }
        canvas.drawText("PocketBrain — Rapport Budget", 24f, 36f, titlePaint)

        val subPaint = Paint().apply { color = Color.parseColor("#E0D7FF"); textSize = 11f }
        val periodLabel = "${dateFmt.format(Date(startMs))}  →  ${dateFmt.format(Date(endMs))}"
        canvas.drawText("Période : $periodLabel", 24f, 58f, subPaint)
        canvas.drawText("Généré le : ${dateFmt.format(Date())}", 24f, 72f, subPaint)

        y = 100f

        // ── SUMMARY ─────────────────────────────────────────────────────────
        val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
        val incomes  = transactions.filter { it.type == TransactionType.INCOME }
        val totalExp = expenses.sumOf { it.amount }
        val totalInc = incomes.sumOf { it.amount }
        val net      = totalInc - totalExp

        y = drawSection(canvas, "Résumé", y, pageWidth.toFloat())
        y = drawKeyValue(canvas, "Revenus totaux", fmt.format(totalInc), y, Color.parseColor("#10B981"))
        y = drawKeyValue(canvas, "Dépenses totales", fmt.format(totalExp), y, Color.parseColor("#EF4444"))
        y = drawKeyValue(canvas, "Solde net", fmt.format(net), y,
            if (net >= 0) Color.parseColor("#10B981") else Color.parseColor("#EF4444"))
        y = drawKeyValue(canvas, "Nb transactions", transactions.size.toString(), y, Color.DKGRAY)
        y += 16f

        // ── POCKETS ─────────────────────────────────────────────────────────
        y = drawSection(canvas, "Répartition des poches", y, pageWidth.toFloat())
        PocketType.entries.forEach { pocket ->
            val spent = expenses.filter { it.pocket == pocket }.sumOf { it.amount }
            val alloc = when (pocket) {
                PocketType.DAILY      -> budget.dailyLifeAmount()
                PocketType.SAVINGS    -> budget.savingsAmount()
                PocketType.INVESTMENT -> budget.investmentAmount()
                PocketType.FUN        -> budget.funAmount()
            }
            val pct = if (alloc > 0) (spent / alloc * 100).toInt() else 0
            y = drawKeyValue(canvas, pocket.labelFr,
                "${fmt.format(spent)} / ${fmt.format(alloc)} ($pct%)", y, Color.DKGRAY)
            y = drawProgressBar(canvas, (spent / alloc.coerceAtLeast(0.01)).toFloat(), y, pageWidth.toFloat())
            y += 8f
        }
        y += 8f

        // ── CATEGORIES ──────────────────────────────────────────────────────
        val catMap = expenses.groupBy { it.category }.mapValues { it.value.sumOf { tx -> tx.amount } }
            .entries.sortedByDescending { it.value }
        if (catMap.isNotEmpty()) {
            y = drawSection(canvas, "Dépenses par catégorie", y, pageWidth.toFloat())
            catMap.forEach { (cat, amt) ->
                y = checkNewPage(pdf, canvas, y, page, pageWidth, pageHeight).also { canvas = it.second; page = it.first }
                y = drawKeyValue(canvas, cat, fmt.format(amt), y, Color.DKGRAY)
            }
            y += 8f
        }

        // ── TRANSACTIONS ────────────────────────────────────────────────────
        val result = checkNewPage(pdf, canvas, y, page, pageWidth, pageHeight)
        canvas = result.second; page = result.first

        y = 40f.takeIf { y > pageHeight - 200 } ?: result.second.let { y }
        y = drawSection(canvas, "Historique des transactions", y, pageWidth.toFloat())

        val rowPaint   = Paint().apply { color = Color.parseColor("#F3F0FF"); style = Paint.Style.FILL }
        val normalPaint = Paint().apply { color = Color.DKGRAY; textSize = 9f }
        val boldPaint   = Paint().apply { color = Color.BLACK; textSize = 9f; typeface = Typeface.DEFAULT_BOLD }
        val greenPaint  = Paint().apply { color = Color.parseColor("#10B981"); textSize = 9f; typeface = Typeface.DEFAULT_BOLD }
        val redPaint    = Paint().apply { color = Color.parseColor("#EF4444"); textSize = 9f; typeface = Typeface.DEFAULT_BOLD }

        transactions.forEachIndexed { idx, tx ->
            // New page if needed
            val pair = checkNewPage(pdf, canvas, y, page, pageWidth, pageHeight)
            canvas = pair.second; page = pair.first
            if (y > pageHeight - 60) y = 40f

            val rowY = y
            if (idx % 2 == 0) canvas.drawRect(20f, rowY - 14f, (pageWidth - 20).toFloat(), rowY + 6f, rowPaint)

            canvas.drawText(dateFmt.format(Date(tx.timestamp)), 22f, rowY, normalPaint)
            canvas.drawText(tx.category, 155f, rowY, boldPaint)
            canvas.drawText(tx.pocket.labelFr, 280f, rowY, normalPaint)
            val amtPaint = if (tx.type == TransactionType.INCOME) greenPaint else redPaint
            val sign = if (tx.type == TransactionType.INCOME) "+" else "-"
            canvas.drawText("$sign${fmt.format(tx.amount)}", 440f, rowY, amtPaint)
            if (tx.note.isNotBlank()) canvas.drawText(tx.note.take(30), 155f, rowY + 12f, normalPaint)
            y += 22f
        }

        pdf.finishPage(page)

        // Save
        val dir  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        dir.mkdirs()
        val file = File(dir, "PocketBrain_${nameDateFmt.format(Date())}.pdf")
        FileOutputStream(file).use { pdf.writeTo(it) }
        pdf.close()
        return file
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private fun startPage(pdf: PdfDocument, num: Int, w: Int, h: Int): PdfDocument.Page {
        val info = PdfDocument.PageInfo.Builder(w, h, num).create()
        return pdf.startPage(info)
    }

    private fun drawSection(canvas: Canvas, title: String, y: Float, pageWidth: Float): Float {
        val paint = Paint().apply { color = Color.parseColor("#7C3AED"); style = Paint.Style.FILL }
        canvas.drawRect(20f, y, pageWidth - 20, y + 24f, paint)
        val tp = Paint().apply { color = Color.WHITE; textSize = 12f; typeface = Typeface.DEFAULT_BOLD }
        canvas.drawText(title, 28f, y + 16f, tp)
        return y + 32f
    }

    private fun drawKeyValue(canvas: Canvas, key: String, value: String, y: Float, valueColor: Int): Float {
        val kp = Paint().apply { color = Color.DKGRAY; textSize = 10f }
        val vp = Paint().apply { color = valueColor; textSize = 10f; typeface = Typeface.DEFAULT_BOLD }
        canvas.drawText(key, 24f, y, kp)
        canvas.drawText(value, 340f, y, vp)
        return y + 18f
    }

    private fun drawProgressBar(canvas: Canvas, progress: Float, y: Float, pageWidth: Float): Float {
        val trackPaint = Paint().apply { color = Color.parseColor("#E5E7EB"); style = Paint.Style.FILL }
        val barPaint   = Paint().apply {
            color = if (progress > 1f) Color.parseColor("#EF4444") else Color.parseColor("#7C3AED")
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(24f, y, pageWidth - 24, y + 8f, 4f, 4f, trackPaint)
        canvas.drawRoundRect(24f, y, 24f + (pageWidth - 48) * progress.coerceIn(0f, 1f), y + 8f, 4f, 4f, barPaint)
        return y + 14f
    }

    private fun checkNewPage(
        pdf: PdfDocument, canvas: Canvas, y: Float,
        page: PdfDocument.Page, pageWidth: Int, pageHeight: Int
    ): Pair<PdfDocument.Page, Canvas> {
        return if (y > pageHeight - 60) {
            pdf.finishPage(page)
            val newPage = startPage(pdf, pdf.pages.size + 1, pageWidth, pageHeight)
            Pair(newPage, newPage.canvas)
        } else {
            Pair(page, canvas)
        }
    }
}
