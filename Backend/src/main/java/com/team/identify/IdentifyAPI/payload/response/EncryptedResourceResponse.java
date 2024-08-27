package com.team.identify.IdentifyAPI.payload.response;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Encapsulates the card data after decryption")
public class EncryptedResourceResponse {
    @Schema(description = "The page number that was requested.")
    private int page;
    @Schema(description = "The maximum page number for this query")
    private int numPages;
    @Schema(description = "JSON array of cards, the fields returned can vary on the type of card.")
    private ArrayNode results;

    public EncryptedResourceResponse(int page, int numPages, ArrayNode results) {
        this.page = page;
        this.numPages = numPages;
        this.results = results;
    }

    public EncryptedResourceResponse() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public ArrayNode getResults() {
        return results;
    }

    public void setResults(ArrayNode results) {
        this.results = results;
    }
}
