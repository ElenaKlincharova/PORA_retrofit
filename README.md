# Retrofit

## Opis
Retrofit je **type-safe HTTP klient** za Android in Java, ki poenostavi delo z REST API-ji. Omogoča enostavno pošiljanje HTTP zahtevkov in pretvorbo JSON odgovorov v Java/Kotlin objekte.  

- Omogoča hitro in enostavno integracijo REST API-jev.  
- Dobro se integrira z Gson ali Moshi za JSON parsing.  
- Ima podporo za asinkrone klice, kar zmanjšuje zamrznitev UI.  
- Široko dokumentiran in vzdrževan.  

## Prednosti
- Enostavna uporaba: enostavno definiramo API klice kot metode.  
- Integracija z JSON parserji: Gson, Moshi.  
- Podpora za RxJava/Coroutines: za asinhrono programiranje.  
- Tip-sigurno: napake pri klicih se pokažejo že med kompilacijo.  
- Preprosta sintaksa z anotacijami (@GET, @POST, @Path, @Query) za definicijo API klicev.
- Vzdrževanje: redne posodobitve, aktivna skupnost.  

## Slabosti
- Ni podpora za GraphQL (čeprav se lahko z dodatki uporabi).  
- Učinkovitost pri velikih količinah podatkov: lahko je počasnejši kot nekateri lažji HTTP klienti (npr. OkHttp neposredno).  
- Odvisnost od OkHttp: Čeprav je to tudi prednost, zahteva dodatno odvisnost in razumevanje dveh knjižnic.
- Večja odvisnost od drugih knjižnic: za JSON parsing in RxJava/Coroutines.  

## Licenca
**Apache License 2.0**  Omogoča prosto uporabo, spreminjanje in distribucijo.

## Število uporabnikov
Na GitHubu ima Retrofit več kot **43k zvezdic**, je eden najbolj priljubljenih omrežnih knjižnic za Android. Uporablja ga večina večjih Android aplikacij.

## Časovna in prostorska zahtevnost
- **Časovna zahtevnost:** odvisna od API-ja in klicev; Retrofit sam je dovolj hiter O(1) za konfiguracijo, asinhroni klicev ne blokirajo glavne niti.
- **Prostorska zahtevnost:** Minimalna (∼1MB dodano k APK), učinkovito upravljanje s povezavami in pomnilnikom; samo knjižnica in JSON parser.  

## Vzdrževanje
- **Število razvijalcev:** aktivna skupnost odprtokodnih prispevalcev, glavni avtor Jake Wharton.  
- **Zadnja sprememba:** redne posodobitve na GitHubu. Zadnja verzija 3.0.0 narejena 15. Maja  
- **Dolgoročna podpora:** zagotovljena zaradi široke uporabe v industrij