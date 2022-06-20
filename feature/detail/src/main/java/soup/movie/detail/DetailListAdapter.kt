/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ext.IdBasedDiffCallback
import coil.compose.rememberAsyncImagePainter
import com.google.android.material.composethemeadapter.MdcTheme
import com.webtoonscorp.android.readmore.material.ReadMoreText
import soup.movie.detail.databinding.DetailItemAdBinding
import soup.movie.detail.databinding.DetailItemBoxOfficeBinding
import soup.movie.detail.databinding.DetailItemCgvBinding
import soup.movie.detail.databinding.DetailItemHeaderBinding
import soup.movie.detail.databinding.DetailItemImdbBinding
import soup.movie.detail.databinding.DetailItemLotteBinding
import soup.movie.detail.databinding.DetailItemMegaboxBinding
import soup.movie.detail.databinding.DetailItemNaverBinding
import soup.movie.util.setOnDebounceClickListener

private typealias OnItemClickListener = (Int) -> Unit

internal class DetailListAdapter(
    onItemClick: (ContentItemUiModel) -> Unit
) : ListAdapter<ContentItemUiModel, DetailListAdapter.ViewHolder>(IdBasedDiffCallback { it.id }) {

    private val itemListener: OnItemClickListener = { position ->
        when (val item = getItem(position)) {
            is PlotItemUiModel -> {
                item.isExpanded = item.isExpanded.not()
                notifyItemChanged(position)
            }
            else -> onItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            detail_item_header ->
                HeaderViewHolder(DetailItemHeaderBinding.inflate(layoutInflater, parent, false))
            detail_item_box_office ->
                BoxOfficeViewHolder(
                    DetailItemBoxOfficeBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    ),
                    itemListener
                )
            detail_item_cgv ->
                CgvViewHolder(
                    DetailItemCgvBinding.inflate(layoutInflater, parent, false),
                    itemListener
                )
            detail_item_lotte ->
                LotteViewHolder(
                    DetailItemLotteBinding.inflate(layoutInflater, parent, false),
                    itemListener
                )
            detail_item_megabox ->
                MegaboxViewHolder(
                    DetailItemMegaboxBinding.inflate(layoutInflater, parent, false),
                    itemListener
                )
            detail_item_naver ->
                NaverViewHolder(
                    DetailItemNaverBinding.inflate(layoutInflater, parent, false),
                    itemListener
                )
            detail_item_imdb ->
                ImdbViewHolder(
                    DetailItemImdbBinding.inflate(layoutInflater, parent, false),
                    itemListener
                )
            detail_item_plot ->
                PlotViewHolder(ComposeView(parent.context), itemListener)
            detail_item_cast ->
                CastViewHolder(ComposeView(parent.context))
            detail_item_trailer_header ->
                TrailerHeaderViewHolder(ComposeView(parent.context), itemListener)
            detail_item_trailer ->
                TrailerViewHolder(ComposeView(parent.context), itemListener)
            detail_item_trailer_footer ->
                TrailerFooterViewHolder(ComposeView(parent.context), itemListener)
            detail_item_ad -> AdViewHolder(
                DetailItemAdBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("This is not valid type.")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            if (headerHeight > 0) {
                holder.itemView.updateLayoutParams { height = headerHeight }
            }
        } else {
            holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is HeaderItemUiModel -> detail_item_header
        is BoxOfficeItemUiModel -> detail_item_box_office
        is CgvItemUiModel -> detail_item_cgv
        is LotteItemUiModel -> detail_item_lotte
        is MegaboxItemUiModel -> detail_item_megabox
        is NaverItemUiModel -> detail_item_naver
        is ImdbItemUiModel -> detail_item_imdb
        is PlotItemUiModel -> detail_item_plot
        is CastItemUiModel -> detail_item_cast
        is TrailerHeaderItemUiModel -> detail_item_trailer_header
        is TrailerItemUiModel -> detail_item_trailer
        is TrailerFooterItemUiModel -> detail_item_trailer_footer
        is AdItemUiModel -> detail_item_ad
    }

    fun getSpanSize(position: Int): Int = when (getItem(position)) {
        is CgvItemUiModel,
        is LotteItemUiModel,
        is MegaboxItemUiModel -> 1
        else -> 3
    }

    private var headerHeight: Int = 0

    fun updateHeader(height: Int) {
        headerHeight = height
        notifyItemChanged(0)
    }

    companion object {
        const val detail_item_header = 1
        const val detail_item_box_office = 2
        const val detail_item_cgv = 3
        const val detail_item_lotte = 4
        const val detail_item_megabox = 5
        const val detail_item_naver = 6
        const val detail_item_imdb = 7
        const val detail_item_plot = 8
        const val detail_item_cast = 9
        const val detail_item_trailer_header = 10
        const val detail_item_trailer = 11
        const val detail_item_trailer_footer = 12
        const val detail_item_ad = 13
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(item: ContentItemUiModel) {}
    }

    class HeaderViewHolder(
        binding: DetailItemHeaderBinding
    ) : ViewHolder(binding.root)

    class BoxOfficeViewHolder(
        private val binding: DetailItemBoxOfficeBinding,
        private val listener: OnItemClickListener
    ) : ViewHolder(binding.root) {

        init {
            itemView.setOnDebounceClickListener {
                listener(bindingAdapterPosition)
            }
        }

        override fun bind(item: ContentItemUiModel) {
            if (item !is BoxOfficeItemUiModel) return
            val context = itemView.context
            binding.rankText.text = context.getString(R.string.rank, item.rank)
            binding.rankDescription.text = context.getString(R.string.rank_date, item.rankDate)
            binding.audienceText.text = context.getString(R.string.audience, item.audience)
            binding.audienceDescription.text =
                context.getString(R.string.screen_days, item.screenDays)
            binding.ratingText.text = item.rating
        }
    }

    class CgvViewHolder(
        private val binding: DetailItemCgvBinding,
        private val listener: OnItemClickListener
    ) : ViewHolder(binding.root) {

        init {
            itemView.setOnDebounceClickListener {
                listener(bindingAdapterPosition)
            }
        }

        override fun bind(item: ContentItemUiModel) {
            if (item !is CgvItemUiModel) return
            itemView.isEnabled = item.hasInfo
            binding.eggLabel.text = item.rating
        }
    }

    class LotteViewHolder(
        private val binding: DetailItemLotteBinding,
        private val listener: OnItemClickListener
    ) : ViewHolder(binding.root) {

        init {
            itemView.setOnDebounceClickListener {
                listener(bindingAdapterPosition)
            }
        }

        override fun bind(item: ContentItemUiModel) {
            if (item !is LotteItemUiModel) return
            itemView.isEnabled = item.hasInfo
            binding.eggLabel.text = item.rating
        }
    }

    class MegaboxViewHolder(
        private val binding: DetailItemMegaboxBinding,
        private val listener: OnItemClickListener
    ) : ViewHolder(binding.root) {

        init {
            itemView.setOnDebounceClickListener {
                listener(bindingAdapterPosition)
            }
        }

        override fun bind(item: ContentItemUiModel) {
            if (item !is MegaboxItemUiModel) return
            itemView.isEnabled = item.hasInfo
            binding.eggLabel.text = item.rating
        }
    }

    class NaverViewHolder(
        private val binding: DetailItemNaverBinding,
        private val listener: OnItemClickListener
    ) : ViewHolder(binding.root) {

        init {
            binding.brandView.setOnDebounceClickListener {
                listener(bindingAdapterPosition)
            }
            binding.infoButton.setOnDebounceClickListener {
                listener(bindingAdapterPosition)
            }
        }

        override fun bind(item: ContentItemUiModel) {
            if (item !is NaverItemUiModel) return
            binding.eggLabel.text = item.rating
        }
    }

    class ImdbViewHolder(
        private val binding: DetailItemImdbBinding,
        private val listener: OnItemClickListener
    ) : ViewHolder(binding.root) {

        init {
            itemView.setOnDebounceClickListener {
                listener(bindingAdapterPosition)
            }
        }

        override fun bind(item: ContentItemUiModel) {
            if (item !is ImdbItemUiModel) return
            binding.imdbRatingText.text = item.imdb
            binding.rtRatingIcon.setTomatoMeterIcon(item.rottenTomatoes)
            binding.rtRatingText.text = item.rottenTomatoes
            binding.metascoreRatingText.text = item.metascore
        }
    }

    class PlotViewHolder(
        private val composeView: ComposeView,
        private val listener: OnItemClickListener
    ) : ViewHolder(composeView) {

        override fun bind(item: ContentItemUiModel) {
            if (item !is PlotItemUiModel) return
            composeView.setContent {
                MdcTheme {
                    Plot(
                        uiModel = item,
                        onClick = {
                            listener(bindingAdapterPosition)
                        }
                    )
                }
            }
        }
    }

    class CastViewHolder(
        private val composeView: ComposeView
    ) : ViewHolder(composeView) {

        override fun bind(item: ContentItemUiModel) {
            if (item !is CastItemUiModel) return
            composeView.setContent {
                MdcTheme {
                    Cast(item)
                }
            }
        }
    }

    class TrailerHeaderViewHolder(
        private val composeView: ComposeView,
        private val listener: OnItemClickListener
    ) : ViewHolder(composeView) {

        override fun bind(item: ContentItemUiModel) {
            if (item !is TrailerHeaderItemUiModel) return
            composeView.setContent {
                MdcTheme {
                    TrailerHeader(
                        uiModel = item,
                        onPrivacyTipClick = {
                            listener(bindingAdapterPosition)
                        }
                    )
                }
            }
        }
    }

    class TrailerViewHolder(
        private val composeView: ComposeView,
        private val listener: OnItemClickListener
    ) : ViewHolder(composeView) {

        override fun bind(item: ContentItemUiModel) {
            if (item !is TrailerItemUiModel) return
            composeView.setContent {
                MdcTheme {
                    TrailerItem(
                        uiModel = item,
                        onClick = { listener(bindingAdapterPosition) }
                    )
                }
            }
        }
    }

    class TrailerFooterViewHolder(
        private val composeView: ComposeView,
        private val listener: OnItemClickListener
    ) : ViewHolder(composeView) {

        override fun bind(item: ContentItemUiModel) {
            if (item !is TrailerFooterItemUiModel) return
            composeView.setContent {
                MdcTheme {
                    TrailerFooter(
                        onClick = { listener(bindingAdapterPosition) }
                    )
                }
            }
        }
    }

    class AdViewHolder(private val binding: DetailItemAdBinding) : ViewHolder(binding.root) {

        override fun bind(item: ContentItemUiModel) {
            if (item !is AdItemUiModel) return
            binding.adView.setNativeAd(item.nativeAd)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Plot(
    uiModel: PlotItemUiModel,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = dimensionResource(R.dimen.detail_card_elevation),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            val isExpanded by derivedStateOf { uiModel.isExpanded }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painterResource(R.drawable.ic_round_plot),
                    contentDescription = null
                )
                Text(
                    text = "줄거리",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
            ReadMoreText(
                text = uiModel.plot,
                expanded = isExpanded,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = tween(durationMillis = 100)),
                readMoreText = "더보기",
                readMoreColor = MaterialTheme.colors.secondary,
                readMoreFontWeight = FontWeight.Bold,
                readMoreMaxLines = 3,
            )
        }
    }
}

@Composable
private fun TrailerHeader(
    uiModel: TrailerHeaderItemUiModel,
    onPrivacyTipClick: () -> Unit,
) {
    Card(
        modifier = Modifier.padding(start = 4.dp, end = 4.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = dimensionResource(R.dimen.detail_card_elevation),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().requiredHeight(48.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo_youtube),
                contentDescription = null,
                modifier = Modifier.requiredWidth(48.dp).fillMaxHeight(),
                contentScale = ContentScale.Inside,
            )
            Text(
                text = stringResource(R.string.trailer_search_result, uiModel.movieTitle),
                maxLines = 1,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 4.dp).weight(1f),
            )
            IconButton(onClick = onPrivacyTipClick) {
                Image(
                    painter = painterResource(R.drawable.ic_privacy_tip),
                    contentDescription = null,
                    modifier = Modifier.requiredWidth(48.dp).fillMaxHeight(),
                    contentScale = ContentScale.Inside,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TrailerItem(
    uiModel: TrailerItemUiModel,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(start = 4.dp, end = 4.dp),
        shape = RectangleShape,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = dimensionResource(R.dimen.detail_card_elevation),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(100.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = uiModel.trailer.thumbnailUrl,
                    placeholder = ColorPainter(color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)),
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .requiredWidth(140.dp)
                    .fillMaxHeight(),
            )
            Column(
                modifier = Modifier.fillMaxSize().padding(start = 12.dp)
            ) {
                Text(
                    text = uiModel.trailer.title,
                    style = MaterialTheme.typography.body2,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = uiModel.trailer.author,
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TrailerFooter(
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = dimensionResource(R.dimen.detail_card_elevation),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().requiredHeight(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "더보기",
                color = MaterialTheme.colors.secondary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
