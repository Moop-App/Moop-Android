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
package soup.movie.data.api.response

import kotlinx.serialization.Serializable
import soup.movie.model.Company

/**
 * @param companyNm 회사 (이름)
 * @param companyPartNm 회사 (역할)
 */
@Serializable
class CompanyResponse(
    val companyNm: String,
    val companyPartNm: String,
)

fun CompanyResponse.asModel(): Company {
    return Company(
        companyNm = companyNm,
        companyPartNm = companyPartNm,
    )
}
