package soup.movie.data;

public class Company {
    private String companyCd; //제작사 코드
    private String companyNm; //제작사명

    public String getCompanyCd() {
        return companyCd;
    }

    public String getCompanyNm() {
        return companyNm;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyCd='" + companyCd + '\'' +
                ", companyNm='" + companyNm + '\'' +
                '}';
    }
}
