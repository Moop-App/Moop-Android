package soup.movie.data.api.response

import kotlinx.serialization.Serializable

/**
 * @param companyNm 회사 (이름)
 * @param companyPartNm 회사 (역할)
 */
@Serializable
data class CompanyResponse(
    val companyNm: String,
    val companyPartNm: String
)
