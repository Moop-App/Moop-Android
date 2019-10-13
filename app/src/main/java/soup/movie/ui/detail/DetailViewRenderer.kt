package soup.movie.ui.detail

import androidx.core.view.isGone
import soup.movie.R
import soup.movie.databinding.DetailActivityBinding

interface DetailViewRenderer {

    fun DetailActivityBinding.render(uiModel: HeaderUiModel) {
        renderHeaderView(uiModel)
        renderShareView(uiModel)
    }

    private fun DetailActivityBinding.renderHeaderView(it: HeaderUiModel) {
        if (it.movie.openDate.isEmpty()) {
            header.run {
                openDateLabel.isGone = true
                openDateText.isGone = true
            }
        }
        val kobis = it.movie.kobis
        if (kobis == null) {
            header.run {
                genreLabel.isGone = true
                genreText.isGone = true
                nationLabel.isGone = true
                nationText.isGone = true
                runningTimeLabel.isGone = true
                runningTimeText.isGone = true
                companyLabel.isGone = true
                companyText.isGone = true
            }
        } else {
            header.run {
                genreText.text = kobis.genres?.joinToString(separator = ", ").orEmpty()
                nationText.text = kobis.nations?.joinToString(separator = ", ").orEmpty()

                if (kobis.showTm > 0) {
                    runningTimeText.apply {
                        text = context.getString(R.string.time_minute, kobis.showTm)
                    }
                } else {
                    runningTimeLabel.isGone = true
                    runningTimeText.isGone = true
                }

                val companies = kobis.companys.orEmpty()
                    .asSequence()
                    .filter { it.companyPartNm.contains("배급") }
                    .map { it.companyNm }
                    .joinToString(separator = ", ")
                if (companies.isBlank()) {
                    companyLabel.isGone = true
                    companyText.isGone = true
                } else {
                    companyText.text = companies
                }
            }
        }
    }

    private fun DetailActivityBinding.renderShareView(it: HeaderUiModel) {
        if (it.movie.openDate.isEmpty()) {
            share.run {
                openDateLabel.isGone = true
                openDateText.isGone = true
            }
        }
        val kobis = it.movie.kobis
        if (kobis == null) {
            share.run {
                genreLabel.isGone = true
                genreText.isGone = true
                nationLabel.isGone = true
                nationText.isGone = true
                runningTimeLabel.isGone = true
                runningTimeText.isGone = true
                companyLabel.isGone = true
                companyText.isGone = true
            }
        } else {
            share.run {
                genreText.text = kobis.genres?.joinToString(separator = ", ").orEmpty()
                nationText.text = kobis.nations?.joinToString(separator = ", ").orEmpty()

                if (kobis.showTm > 0) {
                    runningTimeText.apply {
                        text = context.getString(R.string.time_minute, kobis.showTm)
                    }
                } else {
                    runningTimeLabel.isGone = true
                    runningTimeText.isGone = true
                }

                val companies = kobis.companys.orEmpty()
                    .asSequence()
                    .filter { it.companyPartNm.contains("배급") }
                    .map { it.companyNm }
                    .joinToString(separator = ", ")
                if (companies.isBlank()) {
                    companyLabel.isGone = true
                    companyText.isGone = true
                } else {
                    companyText.text = companies
                }
            }
        }
    }
}
