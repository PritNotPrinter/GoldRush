# Gold Rush - A Collect-the-Coins Game

**ISC4UR Unit 4 Assignment**

## Project Introduction / Overview

**Gold Rush** is an engaging, fast-paced arcade-style game built with Java Swing where players collect coins to accumulate points within a 60-second time limit. This project demonstrates essential computer science concepts including collision detection, GUI design, object-oriented programming principles, and file I/O for persistent data storage.

## What the Project Does

Gold Rush is a single-player coin collection game with the following mechanics:

### Core Gameplay
- **Time Limit**: Players have 60 seconds to collect as many coins as possible
- **Lives System**: Players start with 3 lives; collecting bombs costs 1 life
- **Coin Purse**: A coin purse follows the player's mouse cursor, controlled by mouse movement
- **Collection Mechanic**: Click on coins to add them to your score

### Coin Types & Values
- **Bronze Coins**: 2 points (60% spawn rate) - copper/bronze colored circles
- **Iron Coins**: 5 points (25% spawn rate) - gray colored circles  
- **Gold Coins**: 10 points (15% spawn rate) - bright yellow colored circles

### Object Features
- **Bombs**: Clicking a bomb deducts 25 points and costs 1 life; bombs explode with visual feedback, while destroying nearby coins
- **Power-Ups**: Rare power-ups (0.05% spawn chance per frame) grant 5 seconds of automatic coin collection without requiring clicks; when activated, coins are auto-collected on mouse-over while bombs are ignored
- **Object Lifespan**: Coins and bombs disappear after 3 seconds on screen if uncollected/undetonated

### Advanced Features
- **Coin Physics**: Coins bounce realistically off walls and each other using elastic collision detection
- **Persistent Accounts**: User login credentials are stored across game sessions
- **Score Tracking**: High scores are saved per player with timestamp information
- **Pause Functionality**: Players can pause/resume gameplay at any time

## How to Run the Program

   - Create a new account with a username and password (passwords are case-sensitive)
   - Log in with your credentials
   - Click "Start Game" to begin playing
   - Run `CoinCollectorGame.java`

## Project Goals and Purpose
ISC4UR Unit 4 GUI Application Assignment

### Implementation Requirements
1. **Collision Detection**: Implementing collisions between walls and moving coins
2. **Buttons & Mouse Events**: Creating UI with button listeners and mouse event handling (click to collect)
3. **Abstract Classes & Inheritance**: Using `ShapeObject` as parent class for `Coin`, `Bomb`, and `PowerUp`
4. **GUI Design**: GridBagLayout, multiple buttons, colour/border/design enhancements, text fields
5. **File Input/Output**: Persistent storage of user credentials and score data (passwords.txt, usernames.txt, src/scores)

### Features Added Since 1st Build
1. **Images**: Replaced shape-objects with custom images (for bomb, purse, powerup, and game screen)
2. **Authentication System**: Full login/registration page with persistent user accounts + score retrieval
3. **Power-Up System**: Rare auto-collect power-up that activates on click for 5 seconds
4. **Object Expiry**: Coins and bombs automatically disappear after 3-4 seconds (they have independent timers, doesn't have to be the same)
5. **Coin Bouncing**: Coins collide with each other & change direction based on the collision
6. **Game Reset**: Changed from replay to reset button (replay button no longer starts the game immediately)
7. **Variable Changes**: Changed coin/bomb velocities, game-screen size, spawn rates, sizes
8. **UI Changes**: GridBagLayout login page, rounded borders, button/panel styling

## File Breakdown

#### **CoinCollectorGame.java** (Main)
  Initializes the main game window
  Handles the transition from login page to game screen

#### **LoginPage.java** (Login/Registration)
  Extends JPanel
  Handles user login and registration
  Shows scores from a button-click & username search

#### **GamePanel.java** (Main Game Loop)
  Core game logic/loop/rendering
  Board size & key parameter values
  Mouse tracking
  Starting/Resetting/Pausing the game
  Updating the game by frame
  Coin spawning, collisions, mouseclicks
  Background image

#### **ShapeObject.java** (Abstract Base Class)
  Abstract class for Coin, Bomb, PowerUp, and CollectionPurse
  Coordinates, velocity, colour of objects
  move() method for objet motion and collisions
  Accessor & mutator methods

#### **Coin.java** (Collectible Objects)
  extends ShapeObject
  For the coin objects of the game
  Gold, Silver, and Bronze coins
  Assigns random velocity to coins
  Renders coins, checks for point-value on click

#### **Bomb.java** (Harmful Objects)
  Extends ShapeObject
  For the bombs
  Loads image
  Visual feedback on bomb collection/explosion
  Rendering, expiry, and point penalty on click

#### **PowerUp.java** (Powerup)
  Extends ShapeObject
  PowerUp that let's the player collect coins by mousing over them, and not clicking
  Autocollect that ignores bombs
  Loads image, motion, coin collision, activated status

#### **CollectionPurse.java** (Player-Controlled Object)
  Extends ShapeObject
  This is how players get points and collect coins
  Rendering the purse, following the player's mouse
  Collisions

#### **ScoreManager.java** (Saving & Getting Scores)
  Creates files in the scores folder
  Based on a player's username, saves their scores from a game to the file
  Formatted as: {score value} | {YYYY-MM-DD} {HR:MIN:SEC}
  Handles saving, loading, and formatting of scores
  Also some information about the user's playing (e.g. average score, lowest score, highest score)

#### **BackgroundPainter.java** (Backgrounds)
  Manages backgrounds
  Loads image from predetermined path
  Or, if image isn't available, loads backup colour instead

#### **RoundedBorder.java** (UI)
  Implements Border
  Makes a rounded border from Border with edges of a specified radius, colour, and thickness

### Data Files

#### **usernames.txt**
- **Purpose**: Stores all registered usernames (one per line)

#### **passwords.txt**
- **Purpose**: Stores passwords corresponding to usernames (one per line)

#### **scores/** (Directory)
- **Purpose**: Stores individual score files per player.
- **Naming System**: {player_username}.txt


## Additional Notes and Documentation

### Key Parameters
- **GAME_DURATION_SECONDS** = 60 (game length)
- **INITIAL_LIVES** = 3 (starting lives)
- **SPAWN_RATE** = 20 (spawn every 20 frames)
- **PLAYABLE_HEIGHT** = 550 (game area height, excludes button panel)
- **COIN LIFETIME_FRAMES** = 240 (4 seconds at 60 FPS)
- **BOMB LIFETIME_FRAMES** = 180 (3 seconds at 60 FPS)
- **POWERUP_SPAWN_CHANCE** = 0.0005 (0.05% per frame)

### Possible Future Features
1. **Leaderboard**: Rank scores across all users
2. **Scores Dropdown**: Select a username to view scores of, rather than searching for the username manually
3. **Difficulty Levels**: Easy/Normal/Hard with different spawn rates/object velocities
4. **Power-Up Variety**: Different power-ups with other effects
5. **Achievements**: E.g. 100 coins collected, or 1k score, etc.
6. **Game Themes**: Selectable visual themes (colour palettes & background images)
7. **Particle Effects**: Visual feedback for coin collection
8. **Encrypt passwords.txt**: Security purposes