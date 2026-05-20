package com.sonarshowcase.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Order processor with extremely high cognitive complexity.
 * 
 * MNT-07: Cognitive complexity > 50
 * 
 * @author SonarShowcase
 */
public class OrderProcessor {
    
    /**
     * Default constructor for OrderProcessor.
     */
    public OrderProcessor() {
    }

    /**
     * MNT-07: Extremely high cognitive complexity
     * This method has nested conditions, loops, and complex logic
     * SonarQube S3776 will flag this
     *
     * @param orderData Order data map
     * @param customerType Type of customer
     * @param isPriority Whether order is priority
     * @param hasDiscount Whether order has discount
     * @param region Shipping region
     * @param coupons List of coupon codes
     * @param orderDate Order date
     * @return Processing result string
     */
    public String processOrder(Map<String, Object> orderData, String customerType, 
                               boolean isPriority, boolean hasDiscount, String region,
                               List<String> coupons, Date orderDate) {
        
        int[] itemCountHolder = new int[1];
        BigDecimal total = calculateItemsTotal(orderData, itemCountHolder);
        
        if (total == null) {
            return "Invalid order data";
        }
        
        // More nested complexity for discounts
        total = applyCustomerDiscount(total, customerType, isPriority, hasDiscount);
        // Region-based complexity
        total = addShippingCost(total, region);
        // Coupon processing complexity
        total = applyCoupons(total, coupons);
        // Date-based logic
        total = addWeekendSurcharge(total, orderDate, isPriority);
        
        return "Order processed. Total: $" + total.setScale(2, BigDecimal.ROUND_HALF_UP) + 
                     ", Items: " + itemCountHolder[0];
    }
    
    // Nested complexity starts here
    private BigDecimal calculateItemsTotal(Map<String, Object> orderData, int[] itemCountHolder) {
        if (orderData == null || !orderData.containsKey("items")) {
            return null;
        }
        Object items = orderData.get("items");
        if (!(items instanceof List)) {
            return null;
        }
        List<?> itemList = (List<?>) items;
        if (itemList.isEmpty()) {
            return null;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (Object item : itemList) {
            BigDecimal itemTotal = processItem(item, itemCountHolder);
            if (itemTotal == null) {
                return null;
            }
            total = total.add(itemTotal);
        }
        return total;
    }
    
    private BigDecimal processItem(Object item, int[] itemCountHolder) {
        if (!(item instanceof Map)) {
            return null;
        }
        Map<?, ?> itemMap = (Map<?, ?>) item;
        if (!itemMap.containsKey("price")) {
            return null;
        }
        Object priceObj = itemMap.get("price");
        if (!(priceObj instanceof Number)) {
            return null;
        }
        BigDecimal price = new BigDecimal(priceObj.toString());
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        if (!itemMap.containsKey("quantity")) {
            itemCountHolder[0]++;
            return price;
        }
        Object qtyObj = itemMap.get("quantity");
        if (!(qtyObj instanceof Number)) {
            return null;
        }
        int qty = ((Number) qtyObj).intValue();
        if (qty <= 0) {
            return null;
        }
        itemCountHolder[0] += qty;
        return price.multiply(new BigDecimal(qty));
    }
    
    private BigDecimal applyCustomerDiscount(BigDecimal total, String customerType, boolean isPriority, boolean hasDiscount) {
        if (!hasDiscount || customerType == null) {
            return total;
        }
        if (customerType.equals("VIP")) {
            return total.multiply(new BigDecimal(isPriority ? "0.70" : "0.80"));
        } else if (customerType.equals("GOLD")) {
            return total.multiply(new BigDecimal(isPriority ? "0.80" : "0.85"));
        } else if (customerType.equals("SILVER")) {
            return total.multiply(new BigDecimal(isPriority ? "0.85" : "0.90"));
        } else if (isPriority) {
            return total.multiply(new BigDecimal("0.95"));
        }
        return total;
    }
    
    private BigDecimal addShippingCost(BigDecimal total, String region) {
        if (region == null) {
            return total;
        }
        if (region.equals("US")) {
            if (total.compareTo(new BigDecimal("100")) > 0) {
                // Free shipping
            } else {
                total = total.add(new BigDecimal("9.99"));
            }
        } else if (region.equals("EU")) {
            total = total.add(total.compareTo(new BigDecimal("150")) > 0 ? new BigDecimal("9.99") : new BigDecimal("24.99"));
        } else if (region.equals("ASIA")) {
            total = total.add(total.compareTo(new BigDecimal("200")) > 0 ? new BigDecimal("14.99") : new BigDecimal("34.99"));
        } else {
            total = total.add(new BigDecimal("49.99"));
        }
        return total;
    }
    
    private BigDecimal applyCoupons(BigDecimal total, List<String> coupons) {
        if (coupons == null || coupons.isEmpty()) {
            return total;
        }
        for (String coupon : coupons) {
            if (coupon != null) {
                total = applySingleCoupon(total, coupon);
            }
        }
        return total;
    }
    
    private BigDecimal applySingleCoupon(BigDecimal total, String coupon) {
        if (coupon.startsWith("PERCENT")) {
            try {
                int percent = Integer.parseInt(coupon.substring(7));
                if (percent > 0 && percent <= 50) {
                    BigDecimal discount = new BigDecimal(percent).divide(new BigDecimal("100"));
                    total = total.multiply(BigDecimal.ONE.subtract(discount));
                }
            } catch (NumberFormatException e) {
                // Invalid coupon
            }
        } else if (coupon.startsWith("FIXED")) {
            try {
                BigDecimal fixed = new BigDecimal(coupon.substring(5));
                if (fixed.compareTo(total) < 0) {
                    total = total.subtract(fixed);
                }
            } catch (NumberFormatException e) {
                // Invalid coupon
            }
        } else if (coupon.equals("FREESHIP")) {
            // Already handled
        }
        return total;
    }
    
    private BigDecimal addWeekendSurcharge(BigDecimal total, Date orderDate, boolean isPriority) {
        if (orderDate == null) {
            return total;
        }
        Date now = new Date();
        if (orderDate.before(now) && (orderDate.getDay() == 0 || orderDate.getDay() == 6)) {
            // Weekend order
            if (isPriority) {
                total = total.add(new BigDecimal("4.99"));
            }
        }
        return total;
    }
    
    /**
     * MNT: Another complex method
     */
    /**
     * Validates an order
     *
     * @param order Order data map to validate
     * @return true if order is valid, false otherwise
     */
    public boolean validateOrder(Map<String, Object> order) {
        if (order == null) return false;
        if (!order.containsKey("customer")) return false;
        if (!order.containsKey("items")) return false;
        if (!order.containsKey("payment")) return false;
        if (!order.containsKey("shipping")) return false;
        
        Object customer = order.get("customer");
        if (customer == null) return false;
        if (!(customer instanceof Map)) return false;
        
        Map<?, ?> customerMap = (Map<?, ?>) customer;
        if (!customerMap.containsKey("id")) return false;
        if (!customerMap.containsKey("email")) return false;
        
        Object items = order.get("items");
        if (items == null) return false;
        if (!(items instanceof List)) return false;
        if (((List<?>) items).isEmpty()) return false;
        
        return true;
    }
    
    // ==================== CODE DUPLICATION (Intentional Maintainability Issue) ====================
    
    /**
     * MNT: Duplicated method - same logic as validateOrder but with different name
     * 
     * @param order Order data map to validate
     * @return true if order is valid, false otherwise
     */
    public boolean checkOrderValidity(Map<String, Object> order) {
        if (order == null) return false;
        if (!order.containsKey("customer")) return false;
        if (!order.containsKey("items")) return false;
        if (!order.containsKey("payment")) return false;
        if (!order.containsKey("shipping")) return false;
        
        Object customer = order.get("customer");
        if (customer == null) return false;
        if (!(customer instanceof Map)) return false;
        
        Map<?, ?> customerMap = (Map<?, ?>) customer;
        if (!customerMap.containsKey("id")) return false;
        if (!customerMap.containsKey("email")) return false;
        
        Object items = order.get("items");
        if (items == null) return false;
        if (!(items instanceof List)) return false;
        if (((List<?>) items).isEmpty()) return false;
        
        return true;
    }
    
    /**
     * MNT: Another duplicated validation method
     * 
     * @param orderData Order data map to validate
     * @return true if order is valid, false otherwise
     */
    public boolean isValidOrder(Map<String, Object> orderData) {
        if (orderData == null) return false;
        if (!orderData.containsKey("customer")) return false;
        if (!orderData.containsKey("items")) return false;
        if (!orderData.containsKey("payment")) return false;
        if (!orderData.containsKey("shipping")) return false;
        
        Object customer = orderData.get("customer");
        if (customer == null) return false;
        if (!(customer instanceof Map)) return false;
        
        Map<?, ?> customerMap = (Map<?, ?>) customer;
        if (!customerMap.containsKey("id")) return false;
        if (!customerMap.containsKey("email")) return false;
        
        Object items = orderData.get("items");
        if (items == null) return false;
        if (!(items instanceof List)) return false;
        if (((List<?>) items).isEmpty()) return false;
        
        return true;
    }
    
    /**
     * MNT: Duplicated complex processing logic - similar to processOrder
     * 
     * @param orderData Order data map
     * @param customerType Type of customer
     * @param isPriority Whether order is priority
     * @return Processing result string
     */
    public String handleOrder(Map<String, Object> orderData, String customerType, boolean isPriority) {
        // Duplicated nested complexity
        int[] itemCountHolder = new int[]{0};
        BigDecimal total = calculateOrderTotal(orderData, itemCountHolder);
        
        if (total == null) {
            return "Invalid order data";
        }
        
        // Duplicated discount logic
        total = applyCustomerDiscount(total, customerType, isPriority);
        
        return "Order handled. Total: $" + total.setScale(2, BigDecimal.ROUND_HALF_UP) + 
                 ", Items: " + itemCountHolder[0];
    }
    
    private BigDecimal calculateOrderTotal(Map<String, Object> orderData, int[] itemCountHolder) {
        if (orderData == null || !orderData.containsKey("items")) {
            return null;
        }
        Object items = orderData.get("items");
        if (!(items instanceof List)) {
            return null;
        }
        List<?> itemList = (List<?>) items;
        if (itemList.isEmpty()) {
            return null;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (Object item : itemList) {
            BigDecimal itemTotal = processOrderItem(item, itemCountHolder);
            if (itemTotal == null) {
                return null;
            }
            total = total.add(itemTotal);
        }
        return total;
    }
    
    private BigDecimal processOrderItem(Object item, int[] itemCountHolder) {
        return processItem(item, itemCountHolder);
    }
    
    private BigDecimal applyCustomerDiscount(BigDecimal total, String customerType, boolean isPriority) {
        if (customerType == null) {
            return total;
        }
        BigDecimal multiplier = getDiscountMultiplier(customerType, isPriority);
        if (multiplier != null) {
            return total.multiply(multiplier);
        }
        return total;
    }
    
    private BigDecimal getDiscountMultiplier(String customerType, boolean isPriority) {
        switch (customerType) {
            case "VIP":
                return isPriority ? new BigDecimal("0.70") : new BigDecimal("0.80");
            case "GOLD":
                return isPriority ? new BigDecimal("0.80") : new BigDecimal("0.85");
            case "SILVER":
                return isPriority ? new BigDecimal("0.85") : new BigDecimal("0.90");
            default:
                return null;
        }
    }
}

