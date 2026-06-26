package backend.importer;
import java.util.List;

public class ImportPreviewProductDTO {
    private ImportedProductDTO product;
    private boolean importable;
    private List<String> skipReasons;

    public ImportPreviewProductDTO() {}

    public ImportPreviewProductDTO(
        ImportedProductDTO product,
        boolean importable,
        List<String> skipReasons
    ) {
        this.product = product;
        this.importable = importable;
        this.skipReasons = skipReasons;
    }
    
     public ImportedProductDTO getProduct() {
        return product;
    }

    public void setProduct(ImportedProductDTO product) {
        this.product = product;
    }

    public boolean isImportable() {
        return importable;
    }

    public void setImportable(boolean importable) {
        this.importable = importable;
    }

    public List<String> getSkipReasons() {
        return skipReasons;
    }

    public void setSkipReasons(List<String> skipReasons) {
        this.skipReasons = skipReasons;
    }
}
