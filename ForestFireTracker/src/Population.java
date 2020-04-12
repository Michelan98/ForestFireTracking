public class Population {
    private final String species;
    private final String fname;
    private final float density;
    private final int headcount;
    private final boolean endangered;

    public Population(String species, String fname, float density, int headcount, boolean endangered) {
        this.species = species;
        this.fname = fname;
        this.density = density;
        this.headcount = headcount;
        this.endangered = endangered;
    }

    public String getSpecies() { return this.species; }
    public String getForestName() { return this.fname; }
    public float getDensity() { return this.density;}
    public int getHeadcount() { return this.headcount; }
    public boolean isEndangered() { return this.endangered; }
}
