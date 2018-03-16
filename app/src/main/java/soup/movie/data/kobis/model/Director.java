package soup.movie.data.kobis.model;

public class Director {
    private String peopleNm; //영화감독명

    public String getPeopleNm() {
        return peopleNm;
    }

    @Override
    public String toString() {
        return "Director{" +
                "peopleNm='" + peopleNm + '\'' +
                '}';
    }
}
