INSERT IGNORE INTO Artists (artistName, artistImageFile)
VALUES
('Wolfgang Amadeus Mozart', 'mozart.png'),
('Pyotr Ilyich Tchaikovsky', 'tchaikovsky.png'),
('Frederic Chopin', 'chopin.png'),
('Gioachino Rossini', 'rossini.png'),
('Alan Walker', 'alanwalker.png'),
('Ludwig van Beethoven', 'beethoven.png'),
('Soundeffects', 'Soundeffect.png');

INSERT IGNORE INTO Genre (genreName) VALUES
('Classical'),
('Electronic'),
('Rock'),
('Pop'),
('Jazz'),
('Hip Hop'),
('Blues'),
('Country'),
('Reggae'),
('R&B'),
('Metal'),
('Folk'),
('Punk'),
('Soul'),
('Indie'),
('Alternative'),
('Sound Effect');

INSERT IGNORE INTO Audio (audioName, audioDuration, audioFileName, audioImageFileName, audioPrice, artistID, genreID) VALUES
('1812 Overture', 247, '1812overture.mp3', '1812overture.png', 0, 2, 1),
('Dance of the Sugar Plum Fairy', 119, 'danceofthesugarplumfairy.mp3', 'sugarplum.png', 0, 2, 1),
('Dreamer', 155, 'dreamer.mp3', 'dreamer.png', 0, 5, 2),
('Moonlight Sonata', 335, 'moonlightsonata.mp3', 'moonlightsonata.png', 79.99, 6, 1),
('Nocturne in E-flat Major', 276, 'nocturneine.mp3', 'nocturneine.png', 34.99, 3, 1),
('Rondo Alla Turca', 214, 'rondoallaturca.mp3', 'rondoallaturca.png', 49.99, 1, 1),
('Swan Lake', 250, 'swanlake.mp3', 'swanlake.png', 34.99, 2, 1),
('William Tell Overture', 211, 'williamtelloverture.mp3', 'williamtell.png', 29.99, 4, 1),
('Final Countdown', 18, 'FinalCountdownSoundeffect.mp3', 'Soundeffect.png', 0, 7, 17),
('Fluffing Duck', 24, 'FluffingADuckSoundEffect.mp3', 'Soundeffect.png', 0, 7, 17),
('Bossing', 5, 'KamustaAngBuhayBuhaySoundEffect.mp3', 'Soundeffect.png', 0, 7, 17),
('Last Christmas', 18, 'LastChrismasSoundEffect.mp3', 'Soundeffect.png', 0, 7, 17),
('Spiderman', 12, 'spiderman-meme-song.mp3', 'Soundeffect.png', 0, 7, 17),
('You are my sunshine', 29, 'youaremysunshineSoundEffect.mp3', 'Soundeffect.png', 0, 7, 17);