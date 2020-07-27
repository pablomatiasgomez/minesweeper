# minesweeper

Play the game directly at https://minesweeper.pablomatiasgomez.com.ar

Decisions:

- For simplicity and speed, I only wrote integration tests, so that I test all layers at once, unit tests could easily be created.
- Patch a single cell and return the entire game. Eventually it could be split to small resources if needed. For simplicity (and given that the game content is small) we simply use gzip for the response body.
- Patch based of rfc6902
- For simplicity, very few FE validations.

- Take into account that the app that is running at https://minesweeper.pablomatiasgomez.com.ar is hosted in NYC and therefore the latency could be high (~300ms), which does not mean that the app is running slow.