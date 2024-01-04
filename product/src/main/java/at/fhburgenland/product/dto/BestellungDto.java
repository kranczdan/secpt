package at.fhburgenland.product.dto;

public class BestellungDto {
    private Long id;

    private Long productId;

    private Long customerId;

    private Integer amount;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BestellungDto{" +
                "id=" + id +
                ", productId=" + productId +
                ", customerId=" + customerId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
