import com.solidgate.models.*;
import com.solidgate.services.PaymentCreationService;
import com.solidgate.services.PaymentStatusService;
import com.solidgate.utils.SignatureGenerator;
import com.solidgate.web.PaymentPage;
import com.solidgate.web.PaymentSuccessPage;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.Buffer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class PaymentTests {


    private Retrofit retrofitPayment;
    private Retrofit retrofitStatus;
    private WebDriver driver;


    @Test(testName = "Validate payments")
    public void testPayment() throws Exception {
        // create payment
        PaymentRequest paymentRequest = new PaymentRequest(
                Order.getDefault(
                        UUID.randomUUID().toString(),
                        125,
                        "EUR"

                ),
                PageCustomization.getDefault());

        PaymentCreationService paymentCreationService = retrofitPayment.create(PaymentCreationService.class);

        Call<PaymentResponse> paymentCall = paymentCreationService.createPayment(paymentRequest);

        Response<PaymentResponse> paymentResponse = paymentCall.execute();

        // get success credit card
        CreditCard creditCard = CreditCard.getDefaultSuccess();

        // go web
        driver.get(paymentResponse.body().getUrl());

        // pay
        PaymentPage paymentPage = new PaymentPage(driver);
        String paymentAmount = paymentPage.fillCardNumber(creditCard.getNumber())
                .fillCardExpire(creditCard.getExpirationDate())
                .fillCvc(creditCard.getCvcCode())
                .fillNameOnCard(creditCard.getCardHolder())
                .checkTermsAndConditions(true)
                .getPaymentAmount();

        Assert.assertEquals(paymentAmount, paymentRequest.getOrder().getAmountInCurrency());

        // validate payment at web
        PaymentSuccessPage successPage = paymentPage.pay(PaymentSuccessPage.class);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(successPage.getPaymentStatus(), "Payment successful!");
        softAssert.assertEquals(successPage.getOrderDescription(), paymentRequest.getPageCustomization().getOrderDescription());
        softAssert.assertEquals(successPage.getOrderPrice(), paymentRequest.getOrder().getAmountInCurrency());
        softAssert.assertEquals(successPage.getOrderTitle(), paymentRequest.getPageCustomization().getOrderTitle());
        softAssert.assertTrue(successPage.getPaymentCardNumber().contains(creditCard.getLastFourDigits()));
        softAssert.assertAll("Payment status page validations failed.");

        // validate the payment status via API
        PaymentStatusService paymentStatusService = retrofitStatus.create(PaymentStatusService.class);
        Call<OrderStatusResponse> statusResponseCall = paymentStatusService.getStatus(new OrderStatusRequest(paymentRequest.getOrder().getOrderId()));
        AtomicReference<Response<OrderStatusResponse>> statusResponse = new AtomicReference<>();
                new FluentWait<>(retrofitStatus)
                .withTimeout(Duration.ofSeconds(90))
                .pollingEvery(Duration.ofSeconds(2))
                .until(r -> {
                    try {
                        Response<OrderStatusResponse> statusResponseFinal = statusResponseCall.clone().execute();
                        if (statusResponseFinal.body().getOrder().getStatus().equalsIgnoreCase("settle_ok")) {
                            statusResponse.set(statusResponseFinal);
                            return true;
                        }
                    } catch (IOException e) {
                        log.warn("status error");
                    }
                    return false;
                });

        softAssert.assertEquals(statusResponse.get().body().getOrder().getAmount(), paymentRequest.getOrder().getAmount());
        softAssert.assertEquals(statusResponse.get().body().getOrder().getCurrency(), paymentRequest.getOrder().getCurrency());

        softAssert.assertAll("Payment status API validations failed.");
    }


    @BeforeSuite(alwaysRun = true)
    public void prepare() {
        Interceptor authInterceptor = chain -> {
            Request original = chain.request();

            String requestBodyString = null;

            if (original.body() != null) {
                Buffer buffer = new Buffer();
                original.body().writeTo(buffer);
                requestBodyString = buffer.readUtf8();
            }

            Request requestWithAuth = original.newBuilder()
                    .header("merchant", ProjectProfile.getInstance().publicApiKey())
                    .header("signature", SignatureGenerator.generateSignature(
                            ProjectProfile.getInstance().publicApiKey(),
                            requestBodyString,
                            ProjectProfile.getInstance().privateApiKey()
                    ))
                    .build();
            return chain.proceed(requestWithAuth);
        };

        OkHttpClient client = null;
        try {
            client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .sslSocketFactory(getUnsafeSslSocketFactory(), getUnsafeTrustManager())
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        retrofitPayment = new Retrofit.Builder()
                .baseUrl(ProjectProfile.getInstance().paymentPageUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        retrofitStatus = new Retrofit.Builder()
                .baseUrl(ProjectProfile.getInstance().paymentStatusUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);
    }


    @AfterSuite
    public void cleanup() {
        driver.quit();
    }


    public static SSLSocketFactory getUnsafeSslSocketFactory() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {


                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }


                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }


                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        return sslContext.getSocketFactory();
    }


    public static X509TrustManager getUnsafeTrustManager() {
        return (X509TrustManager) new TrustManager[]{
                new X509TrustManager() {


                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }


                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }


                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        }[0];
    }


}
