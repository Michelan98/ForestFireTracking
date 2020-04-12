import java.sql.Timestamp;

public class ForestFire {
    private final Timestamp start_time;
    private final Timestamp end_time;
    private final float surface_area;
    private final String fname;

    public ForestFire(Timestamp start_time, Timestamp end_time, float surface_area, String fname) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.surface_area = surface_area;
        this.fname = fname;
    }

    public Timestamp getStartTime() { return this.start_time; }
    public Timestamp getEndTime() { return this.end_time; }
    public float getSurfaceArea() { return this.surface_area; }
    public String getForestName() { return  this.fname; }

    public String toString() {
        return String.format("start: %s\tend: %s\tarea: %f\tforest: %s",
                start_time.toString(), end_time.toString(), surface_area, fname);
    }
}

