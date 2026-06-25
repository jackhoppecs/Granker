package backend.importer;

public class ImportResultDTO {
    private int fetched;
    private int imported;
    private int skippedDuplicates;
    private int skippedInvalid;

    public ImportResultDTO(){

    }

    public ImportResultDTO(int fetched, int imported, int skippedDuplicates, int skippedInvalid){
        this.fetched = fetched;
        this.imported = imported;
        this.skippedDuplicates = skippedDuplicates;
        this.skippedInvalid = skippedInvalid;
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

    public int getSkippedDuplicates() {
        return skippedDuplicates;
    }

    public void setSkippedDuplicates(int skippedDuplicates) {
        this.skippedDuplicates = skippedDuplicates;
    }

    public int getSkippedInvalid() {
        return skippedInvalid;
    }

    public void setSkippedInvalid(int skippedInvalid) {
        this.skippedInvalid = skippedInvalid;
    }
}