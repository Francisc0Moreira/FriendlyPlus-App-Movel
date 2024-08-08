    package estg.ipp.pt.friendly.ui.Entity
    
    
    import com.google.gson.GsonBuilder
    import okhttp3.OkHttpClient
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory
    import java.security.SecureRandom
    import java.security.cert.X509Certificate
    import javax.net.ssl.SSLContext
    import javax.net.ssl.TrustManager
    import javax.net.ssl.X509TrustManager
    
    object RetrofitHelper{
    
        val baseURL = "https://friendlyplus.azurewebsites.net/api/"
    
        fun getInstance(): Retrofit {
            val trustAllCerts: Array<TrustManager> = arrayOf(
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            )
    
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
    
            return Retrofit.Builder()
                .baseUrl(baseURL)
                .client(
                    OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                        .hostnameVerifier { _, _ -> true }
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .build()
        }
    
    
    }
    
