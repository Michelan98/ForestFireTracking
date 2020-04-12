import java.sql.Timestamp;
import java.util.UUID;

public class PopulationSample {
    private final UUID sample_id;
    private final Timestamp sampling_time;
    private final int sample_headcount;
    private final String species;
    private final String fname;

    public PopulationSample(UUID sample_id, Timestamp sampling_time, int sample_headcount, String species, String fname) {
        this.sample_id = sample_id;
        this.sampling_time = sampling_time;
        this.sample_headcount = sample_headcount;
        this.species = species;
        this.fname = fname;
    }

    public UUID getSampleID() { return this.sample_id; }
    public Timestamp getSamplingTime() { return this.sampling_time; }
    public int getSampleHeadcount() { return this.sample_headcount; }
    public String getSpecies() { return this.species; }
    public String getForestName() { return this.fname; }
}

