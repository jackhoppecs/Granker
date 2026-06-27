package backend.importer;

public class ImportResultDTO {
    private int fetched;
    private int imported;
    private int skippedDuplicatesCount;
    private int skippedInvalidCount;

    public ImportResultDTO(){

    }

    public ImportResultDTO(int fetched, int imported, int skippedDuplicatesCount, int skippedInvalidCount){
        this.fetched = fetched;
        this.imported = imported;
        this.skippedDuplicatesCount = skippedDuplicatesCount;
        this.skippedInvalidCount = skippedInvalidCount;
    }

    public int getFetched() {
        return fetched;
    }

    public void setFetched(int fetched) {
        this.fetched = fetched;
    }

    public int getImported() {
        return imported;
    }

    public void setImported(int imported) {
        this.imported = imported;
    }

    public int getSkippedDuplicatesCount() {
        return skippedDuplicatesCount;
    }

    public void setSkippedDuplicatesCount(int skippedDuplicatesCount) {
        this.skippedDuplicatesCount = skippedDuplicatesCount;
    }

    public int getSkippedInvalidCount() {
        return skippedInvalidCount;
    }

    public void setSkippedInvalidCount(int skippedInvalidCount) {
        this.skippedInvalidCount = skippedInvalidCount;
    }
}