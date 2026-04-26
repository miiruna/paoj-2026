# Proiect Individual — Bibliotecă

**Tema 2: Bibliotecă** (secțiuni, cărți, autori, cititori, împrumuturi)

---

## 1.1 — Lista cu 10 acțiuni / interogări posibile în sistem

1. **Adaugă o carte nouă în bibliotecă** — înregistrează o carte cu titlu, autor, secțiune, ISBN și număr de exemplare
2. **Înregistrează un cititor nou** — adaugă un cititor cu nume, prenume și email
3. **Împrumută o carte unui cititor** — creează un împrumut dacă există exemplare disponibile
4. **Returnează o carte** — marchează împrumutul ca returnat și crește numărul de exemplare disponibile
5. **Caută cărți după autor** — returnează toate cărțile scrise de un autor dat
6. **Listează toate cărțile dintr-o secțiune** — afișează cărțile dintr-o secțiune specificată
7. **Afișează istoricul împrumuturilor unui cititor** — listează toate împrumuturile (active și returnate) ale unui cititor
8. **Verifică disponibilitatea unei cărți** — indică dacă există exemplare disponibile pentru o carte
9. **Afișează cărțile cu cele mai multe împrumuturi** — sortează și afișează cărțile după popularitate
10. **Elimină un cititor din sistem** — șterge un cititor (dacă nu are împrumuturi active)

---

## 1.2 — Lista cu 8 tipuri de obiecte din domeniu

| Clasă       | Descriere                                                              |
|-------------|------------------------------------------------------------------------|
| `Persoana`  | Clasă abstractă de bază pentru Autor și Cititor                        |
| `Autor`     | Persoană care a scris una sau mai multe cărți                          |
| `Cititor`   | Persoană înregistrată care poate împrumuta cărți                       |
| `ISBN`      | Clasă imutabilă care reprezintă identificatorul unic al unei cărți     |
| `Carte`     | Entitatea principală, conținând titlu, autor, secțiune și disponibilitate |
| `Sectiune`  | Secțiunea tematică din bibliotecă (ex: Ficțiune, Știință, etc.)        |
| `Imprumut`  | Înregistrarea unui împrumut (cititor, carte, dată, scadență, returnare)|
| `Rezervare` | Rezervarea unei cărți indisponibile de către un cititor                |
