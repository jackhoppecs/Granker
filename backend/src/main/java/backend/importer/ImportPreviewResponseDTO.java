package backend.importer;

import java.util.List;

public class ImportPreviewResponseDTO {
    private String category;
    private String displayName;
    private int fetchedCount;
    private int importableCount;
    private int skippedCount;
    private List<ImportPreviewProductDTO> products;

    public ImportPreviewResponseDTO() {}

    public ImportPreviewResponseDTO(
        String category,
        String displayName,
        int fetchedCount,
        int importableCount,
        int skippedCount,
        List<ImportPreviewProductDTO> products
    ) {
        this.category = category;
        this.displayName = displayName;
        this.fetchedCount = fetchedCount;
        this.importableCount = importableCount;
        this.skippedCount = skippedCount;
        this.products = products;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getDisplayName(){
        return displayName;
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    public int getFetchedCount(){
        return fetchedCount;
    }

    public void setFetchedCount(int fetchedCount){
        this.fetchedCount = fetchedCount;
    }

    public int getImportableCount(){
        return importableCount;
    }

    public void setImportableCount(int importableCount){
        this.importableCount = importableCount;
    }

    public int getSkippedCount(){
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount){
        this.skippedCount = skippedCount;
    }

    public List<ImportPreviewProductDTO> getproducts(){
        return products;
    }

    public void setproducts(List<ImportPreviewProductDTO> products){
        this.products = products;
    }
}
