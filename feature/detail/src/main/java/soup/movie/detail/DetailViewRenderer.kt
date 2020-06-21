package soup.movie.detail

import androidx.core.view.isVisible
import soup.movie.detail.databinding.DetailActivityBinding
import soup.movie.detail.databinding.DetailHeaderBinding
import soup.movie.detail.databinding.DetailShareBinding
import soup.movie.ext.*

interface DetailViewRenderer {

    fun DetailActivityBinding.render(uiModel: HeaderUiModel) {
        header.render(uiModel)
        share.render(uiModel)
    }

    private fun DetailHeaderBinding.render(uiModel: HeaderUiModel) {
        titleView.text = uiModel.movie.title

        val item = uiModel.movie
        ageBgView.root.setBackgroundResource(item.getAgeBackground())
        ageView.root.asyncText(item.getSimpleAgeLabel())
        newView.root.isVisible = item.isNew()
        bestView.root.isVisible = item.isBest()
        dDayView.root.isVisible = item.isDDay()
        dDayView.root.asyncText(item.getDDayLabel())

        openDateText.text = uiModel.movie.openDate
        val openDateVisible = uiModel.movie.openDate.isNotEmpty()
        openDateLabel.isVisible = openDateVisible
        openDateText.isVisible = openDateVisible

        ageText.text = uiModel.movie.getAgeLabel()

        val genres = uiModel.movie.genres
        if (genres != null) {
            genreText.text = genres.joinToString(separator = ", ")
        }
        val genresVisible = genres != null
        genreLabel.isVisible = genresVisible
        genreText.isVisible = genresVisible

        val nations = uiModel.nations
        nationText.text = nations.joinToString(separator = ", ")
        val nationsVisible = nations.isNotEmpty()
        nationLabel.isVisible = nationsVisible
        nationText.isVisible = nationsVisible

        val showTm = uiModel.showTm
        val showTmVisible = showTm > 0
        if (showTm > 0) {
            runningTimeText.apply {
                text = context.getString(R.string.time_minute, uiModel.showTm)
            }
        }
        runningTimeLabel.isVisible = showTmVisible
        runningTimeText.isVisible = showTmVisible

        val companies = uiModel.companies
            .asSequence()
            .filter { it.companyPartNm.contains("배급") }
            .map { it.companyNm }
            .joinToString(separator = ", ")
        val companiesVisible = companies.isNotBlank()
        if (companiesVisible) {
            companyText.text = companies
        }
        companyLabel.isVisible = companiesVisible
        companyText.isVisible = companiesVisible
    }

    private fun DetailShareBinding.render(uiModel: HeaderUiModel) {
        titleView.text = uiModel.movie.title
        posterCard.loadAsync(uiModel.movie.posterUrl)

        val item = uiModel.movie
        ageBgView.root.setBackgroundResource(item.getAgeBackground())
        ageView.root.asyncText(item.getSimpleAgeLabel())
        newView.root.isVisible = item.isNew()
        bestView.root.isVisible = item.isBest()
        dDayView.root.isVisible = item.isDDay()
        dDayView.root.asyncText(item.getDDayLabel())

        openDateText.text = uiModel.movie.openDate
        val openDateVisible = uiModel.movie.openDate.isNotEmpty()
        openDateLabel.isVisible = openDateVisible
        openDateText.isVisible = openDateVisible

        ageText.text = uiModel.movie.getAgeLabel()

        val genres = uiModel.movie.genres
        if (genres != null) {
            genreText.text = genres.joinToString(separator = ", ")
        }
        val genresVisible = genres != null
        genreLabel.isVisible = genresVisible
        genreText.isVisible = genresVisible

        val nations = uiModel.nations
        nationText.text = nations.joinToString(separator = ", ")
        val nationsVisible = nations.isNotEmpty()
        nationLabel.isVisible = nationsVisible
        nationText.isVisible = nationsVisible

        val showTm = uiModel.showTm
        val showTmVisible = showTm > 0
        if (showTm > 0) {
            runningTimeText.apply {
                text = context.getString(R.string.time_minute, uiModel.showTm)
            }
        }
        runningTimeLabel.isVisible = showTmVisible
        runningTimeText.isVisible = showTmVisible

        val companies = uiModel.companies
            .asSequence()
            .filter { it.companyPartNm.contains("배급") }
            .map { it.companyNm }
            .joinToString(separator = ", ")
        val companiesVisible = companies.isNotBlank()
        if (companiesVisible) {
            companyText.text = companies
        }
        companyLabel.isVisible = companiesVisible
        companyText.isVisible = companiesVisible
    }
}
