import static org.junit.jupiter.api.Assertions.assertEquals;

import com.craft.CraftDemo;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class FraudDetectionTest {

    @Test
    public void testDeclineRule() {
        // Test case for the Decline rule
        Map<String, Object> merchantData = new HashMap<>();
        merchantData.put("IdentityScore", 500.0);
        merchantData.put("EAScore", 70.0);
        merchantData.put("OwnerPhoneTypeIndicator", "W");
        merchantData.put("OwnerMVNOIndicator", "Y");
        merchantData.put("OwnerVerifiedComponents", 2);

        JsonObject result = CraftDemo.applyBusinessRules(merchantData);
        assertEquals("Decline", result.get("decision").getAsString());
    }

    @Test
    public void testManualReviewRule() {
        // Test case for the Manual Review rule
        Map<String, Object> merchantData = new HashMap<>();
        merchantData.put("IdentityScore", 360.0);
        merchantData.put("EAScore", 85.0);
        merchantData.put("OwnerPhoneTypeIndicator", "W");
        merchantData.put("OwnerMVNOIndicator", "Y");
        merchantData.put("OwnerVerifiedComponents", 1);

        JsonObject result = CraftDemo.applyBusinessRules(merchantData);
        assertEquals("Manual Review", result.get("decision").getAsString());
    }

    @Test
    public void testApproveRule() {
        // Test case for the Approve rule
        Map<String, Object> merchantData = new HashMap<>();
        merchantData.put("IdentityScore", 90.0);
        merchantData.put("EAScore", 40.0);
        merchantData.put("OwnerPhoneTypeIndicator", "W");
        merchantData.put("OwnerMVNOIndicator", "N");
        merchantData.put("OwnerVerifiedComponents", 3);

        JsonObject result = CraftDemo.applyBusinessRules(merchantData);
        assertEquals("Approve", result.get("decision").getAsString());
    }

    @Test
    public void testUndeterminedRule() {
        // Test case for an undetermined decision (none of the rules match)
        Map<String, Object> merchantData = new HashMap<>();
        merchantData.put("IdentityScore", 250.0);
        merchantData.put("EAScore", 70.0);
        merchantData.put("OwnerPhoneTypeIndicator", "W");
        merchantData.put("OwnerMVNOIndicator", "N");
        merchantData.put("OwnerVerifiedComponents", 2);

        JsonObject result = CraftDemo.applyBusinessRules(merchantData);
        assertEquals("Undetermined", result.get("decision").getAsString());
    }
}
