# Retrofit

## Opis
Retrofit je **type-safe HTTP klient** za Android in Java, ki poenostavi delo z REST API-ji. Omogoča enostavno pošiljanje HTTP zahtevkov in pretvorbo JSON odgovorov v Java/Kotlin objekte.  

- Omogoča hitro in enostavno integracijo REST API-jev.   
- Ima podporo za asinkrone klice, kar preprečuje zamrznitev UI.  
- Široko dokumentiran in vzdrževan z aktivno skupnostjo.  

## Prednosti
- **Enostavna uporaba:** API klice definiramo kot interface metode z anotacijami.  
- **Integracija s konverterji:** Gson, Moshi, Jackson, XML.  
- **Podpora za asinhrono programiranje:** RxJava, Coroutines, Flow, CompletableFuture.  
- **Type-safe:** napake pri klicih se odkrijejo že med kompilacijo.  
- **Preprosta sintaksa:** anotacije (@GET, @POST, @PUT, @PATCH, @DELETE, @HEAD, @Path, @Query, @Body, @Field, @Header) za definicijo API klicev.
- **Aktivno vzdrževanje:** redne posodobitve, velika skupnost uporabnikov.  
- **Razširljivost:** možnost dodajanja interceptorjev (logging, authentication, caching).  

## Slabosti
- **Ni neposredne podpore za GraphQL:** potrebni so dodatni adapterji (npr. Apollo GraphQL je boljša izbira za GraphQL).  
- **Dodatna odvisnost:** zahteva OkHttp in konverter (npr. Gson), kar poveča velikost APK.  
- **Učna krivulja:** začetniki lahko potrebujejo čas za razumevanje anotacij in asinhronega programiranja.  
- **Manj primeren za binarne podatke:** čeprav je možno, ni optimiziran za prenos velikih datotek ali streaminga.  

## Licenca
**Apache License 2.0**  Omogoča prosto uporabo, spreminjanje in distribucijo.

## Število uporabnikov
Na GitHubu ima Retrofit več kot **43.000 zvezdic** in je ena najbolj priljubljenih omrežnih knjižnic za Android. Uporablja jo večina večjih Android aplikacij (Google, Twitter).

## Časovna in prostorska zahtevnost
- **Časovna zahtevnost:** 
  - Konfiguracija: O(1) Konstantna časovna kompleksnost – enkratna inicializacija Retrofit instance.
  - API klici: Odvisno od omrežja in API-ja; Retrofit sam dodaja minimalno overhead.
  - Obdelava odgovora: Deserializacija JSON podatkov ima linearno časovno zahtevnost O(n), kjer je n velikost prejetih podatkov.
  - Asinhroni klici ne blokirajo glavne niti (uporaba OkHttp thread pool) kar izboljša odzivnost aplikacije.
  
- **Prostorska zahtevnost:** 
  - Retrofit knjižnica: ~70 KB
  - OkHttp: ~500 KB
  - Gson konverter: ~250 KB
  - **Skupaj:** ~1 MB dodano k APK velikosti (odvisno od uporabljenih konverterjev).

## Vzdrževanje
- **Glavni avtor:** Jake Wharton (Square, Inc.)
- **Število razvijalcev:** Aktivna skupnost odprtokodnih prispevalcev (Square team + community).  
- **Zadnja verzija:** **2.9.0** (izdana 5. maja 2020)

## Demo Aplikacija
Demo Aplikacija ki prikazuje vse ključne Retrofit funkcionalnosti z uporabo DummyJSON API za pridobivanje realnih podatkov o produktih.

<video src="https://github.com/user-attachments/assets/85f1f591-45f6-415e-8bea-853d2840e5c2" controls style="max-width: 730px;"></video>

Demonstrirane funkcionalnosti:

- **@GET** - Pridobivanje podatkov (vsi produkti, specifičen produkt, search)
    - **@Path** - Dinamični URL segmenti
    - **@Query** - URL query parametri
- **@POST** - Kreiranje novih produktov
    - **@Body** - Pošiljanje JSON objekta
    - **@Field** - Form-encoded podatki
- **@PUT** - Popolna posodobitev produkta
- **@PATCH** - Delna posodobitev (samo cena)
- **@DELETE** - Brisanje produkta
- **@HEAD** - Preverjanje obstoja
- **ERROR DEMO** - Obravnava napak (404, 500, network timeout, no connection)

## Uporaba pri Drugi Aplikaciji KavarneForYou
V aplikaciji KavarneForYou je Retrofit uporabljen za pridobivanje podatkov o kavarnah in barih iz Overpass API (OpenStreetMap).

### Implementacija

- **API Service Interface**
    - **@GET** - HTTP GET request na endpoint `/api/interpreter`
    - **@Query("data")** - URL query parameter, ki vsebuje Overpass query
    - **suspend** - Kotlin korutina funkcija za asinhrono izvajanje
    - **OverpassResponse** - Avtomatska deserializacija JSON → Kotlin objekt
- **Data Modeli z Gson Anotacijami**
    - **@SerializedName** - mapiranje JSON ključev z dvopičjem (npr. "addr:street") v Kotlin properties
- **Retrofit Client Konfiguracija**
    - **Base URL** - osnovna URL za vse API klice
    - **OkHttp Client** - custom timeout nastavitve (30 sekund zaradi počasnega API-ja)
    - **GsonConverterFactory** - avtomatska JSON serializacija/deserializacija
    - **retrofit.create()** - dinamično kreiranje implementacije interface-a

- **Pomembne Odvisnosti**
    - **Retrofit**                                                                                                                                        
    implementation("com.squareup.retrofit2:retrofit:2.9.0")                                                                                               
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    - **Coroutines (za suspend funkcije)**                                                                                                                
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    - **Lifecycle (za lifecycleScope)**                                                                                                                   
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")