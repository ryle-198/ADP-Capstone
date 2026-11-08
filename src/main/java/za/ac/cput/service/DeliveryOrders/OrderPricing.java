package za.ac.cput.service.DeliveryOrders;

public final class OrderPricing {

    public static final float BASE_FEE = 50f;
    public static final float RATE_PER_KG = 10f;

    private OrderPricing() {
    }

    public static float calculate(float weightKg) {
        float price = BASE_FEE + RATE_PER_KG * Math.max(weightKg, 0f);
        return Math.round(price * 100f) / 100f;
    }
}
