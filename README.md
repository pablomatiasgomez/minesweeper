# minesweeper

Play the game directly at https://minesweeper.pablomatiasgomez.com.ar

Decisions:

- For simplicity and speed, I only wrote integration tests, so that I test all layers at once, unit tests could easily be created.
- Patch a single cell and return the entire game. Eventually it could be split to small resources if needed.
- Patch based of rfc6902
