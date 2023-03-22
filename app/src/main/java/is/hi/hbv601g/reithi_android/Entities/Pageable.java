package is.hi.hbv601g.reithi_android.Entities;

public class Pageable {
    private int offset;
    private int pageNumber;
    private int pageSize;
    private Sort sort;
    private boolean unpaged;
    private boolean paged;

    public Pageable(int offset, int pageNumber, int pageSize, Sort sort, boolean unpaged, boolean paged) {
        this.offset = offset;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
        this.unpaged = unpaged;
        this.paged = paged;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public boolean isUnpaged() {
        return unpaged;
    }

    public void setUnpaged(boolean unpaged) {
        this.unpaged = unpaged;
    }

    public boolean isPaged() {
        return paged;
    }

    public void setPaged(boolean paged) {
        this.paged = paged;
    }

    // getters and setters
}
