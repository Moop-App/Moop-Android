package soup.movie.data.model;

import com.google.gson.annotations.SerializedName;

public class CodeResponse {

    @SerializedName("cgv")
    private CodeGroup cgvGroup;

    @SerializedName("lotte")
    private CodeGroup lotteGroup;

    @SerializedName("megabox")
    private CodeGroup megaboxGroup;

    public CodeResponse() {
    }

    public CodeGroup getCgvGroup() {
        return cgvGroup;
    }

    public CodeGroup getLotteGroup() {
        return lotteGroup;
    }

    public CodeGroup getMegaboxGroup() {
        return megaboxGroup;
    }

    @Override
    public String toString() {
        return "CodeResponse{" +
                "cgvGroup=" + cgvGroup +
                ", lotteGroup=" + lotteGroup +
                ", megaboxGroup=" + megaboxGroup +
                '}';
    }
}
