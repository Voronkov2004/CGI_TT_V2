# CGI_TT_V2

## Starting:

1. Selleks, et käivitada veebirakendust, on vaja vahetada andmebaasi omadused (port, andmebaasi nimi, andmebaasi salasõna) failis CGI_TT_V2 / src / main / resources / <ins>application.properties</ins>. 

2. Peale seda, pea kaustas (CGI_TT_V2), on vaja sisestada ja käivitada terminalis käsk <ins>./gradlew build</ins>. 

3. Pärast on vaja käivitada pea fail cgi_TT / src / main / java / org / testtask / cgi_tt / <ins>CgiTtApplication.java</ins>. 

4. Pärast seda avame cgitt_web terminali ja käivitame järgmised käsud: 1. <ins>npm install</ins> ja <ins>npm run dev</ins> (sellega käivitame front-endi).

5. Kui need sammud on tehtud, Liquibase teeb andmebaasis uue tabeli, kuhu sisestab mõned testandmed. 

6. Lõpuks saame vaadata localhost:8080 (back-end API) ja localhost:5173, et vaadata lõplikku veebirakendust.

## Docs
Selle projekti tegemiseks läks mul kolm päeva. Esimesel päeval tegin back-end API ja seadistasin Liquibase'i, järgmised kaks päeva tegelesin front-end-iga ning back-endi lisamisega.

ChatGPT-d kasutasin selleks, et teha istekohtade valimise front-end (ainult stiilid, loogika kirjutasin ise) ning see aitas mind vigade parandamisel soovituse algoritmi kirjutamisel.

Võimalikud filtrid, mida ma lisasin: otsing sihtkoha ja lähtekoha järgi (mõlemad asukohad peavad olema määratud), otsing kuupäeva järgi (kui väljumiskuupäev ja saabumiskuupäev on määratud), otsing kuupäeva, sihtkoha ja lähtekoha järgi, otsing kõigi parameetrite järgi (nagu eelmine, aga kui inimeste arv on sisestatud). See tagastab kõik reisid, kus maksimaalne inimeste arv on suurem kui broneeritud istekohtade arv + inimeste arv. Samuti saab otsida ainult inimeste arvu alusel.

Ma tegin juba paar sarnast testülesannet ja sellega mul palju raskusi ei olnud. Ainus, mida tahan esile tõsta, on see, et seekord proovisin kasutada Liquibase'i ja mul läks üsna palju aega selle seadistamiseks ja sellest tehnoloogiast aru saamiseks.
