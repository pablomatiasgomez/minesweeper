# minesweeper


Decisions:

- For simplicity and speed, I only wrote integration tests, so that I test all layers at once, unit tests could easily be created.
- Patch the entire game and return entire entity because it's small. Eventually it could be split to small resources if needed.
- Patch based of rfc6902
