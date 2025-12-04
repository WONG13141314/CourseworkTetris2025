# TetrisJFX - Maintenance & Extension Documentation

**Student Name:** WONG YUNG TUNG  
**Course:** COMP2042

## GitHub Repository

**Repository Link:** https://github.com/WONG13141314/CourseworkTetris2025

---

## Compilation Instructions

### Prerequisites

- **Java Development Kit (JDK):** Version 11 or higher
- **JavaFX SDK:** Version 11 or higher
- **Maven:** Version 3.6 or higher (for dependency management)

### Steps to Compile and Run

1. **Clone the Repository**
```bash
git clone https://github.com/WONG13141314/CourseworkTetris2025
cd TetrisJFX
```

2. **Build the Project**
```bash
mvn clean install
```

3. **Run the Application**
```bash
mvn javafx:run
```

**Alternative:** If using an IDE (IntelliJ IDEA/Eclipse):
- Import the project as a Maven project
- Configure JavaFX SDK in project settings
- Run `Main.java` as the main class

### Dependencies

The project uses the following key dependencies (managed via Maven):
- JavaFX Controls
- JavaFX FXML
- Java Sound API (built-in)
- Java Preferences API (built-in)

---

## Features Implementation Status

### Implemented and Working Properly

| Feature | Description | Status |
|---------|-------------|--------|
| **Main Menu System** | Complete main menu with mode selection and high score display | Working |
| **Game Mode - ZEN** | Endless mode with no time limit, board clears on game over | Working |
| **Game Mode - BLITZ** | 2-minute timed mode with progressive difficulty levels | Working |
| **Hold Brick Feature** | Players can hold one piece and swap it later (C/Shift key) | Working |
| **Hard Drop** | Instant piece placement (Spacebar) | Working |
| **Shadow/Ghost Piece** | Visual indicator showing where piece will land | Working |
| **DAS/ARR Input System** | Delayed Auto Shift and Auto Repeat Rate for smooth controls | Working |
| **Wall Kick Rotation** | Automatic position adjustment during rotation near walls | Working |
| **Score System** | Persistent high scores for both game modes | Working |
| **Background Music** | Looping background music with proper transitions | Working |
| **Sound Effects** | Clear row and game over sound effects | Working |
| **Pause Functionality** | Game can be paused/resumed (P key) | Working |
| **Responsive UI** | Scalable window with maintained aspect ratio | Working |
| **Preview Panels** | Next piece and hold piece preview displays | Working |
| **Level Progression (Blitz)** | Dynamic speed increase with level ups | Working |
| **Visual Feedback** | Score notifications and animations | Working |
| **7-Bag Randomizer** | Fair piece distribution system | Working |

### Implemented but Not Working Properly

| Feature | Issue Description | Attempted Solution |
|---------|------------------|-------------------|
| None | All implemented features are functioning correctly | N/A |

### Features Not Implemented

| Feature | Reason Not Implemented |
|---------|----------------------|
| **Multiplayer Mode** | Time constraints; would require significant networking infrastructure and synchronization logic |
| **Touch Controls** | Project focused on desktop experience; touch controls would require different UI paradigm |
| **Customizable Controls** | Priority given to other features; default controls are intuitive and well-documented |
| **Achievement System** | While interesting, focus was on core gameplay mechanics and mode diversity |
| **Replay System** | Complex feature requiring recording and playback infrastructure; deprioritized |

---

## Game Controls

| Action | Key |
|--------|-----|
| Move Left | Left Arrow / A |
| Move Right | Right Arrow / D |
| Soft Drop | Down Arrow / S |
| Hard Drop | Spacebar |
| Rotate Clockwise | Up Arrow / W |
| Rotate Counter-Clockwise | Z |
| Hold Piece | C / Shift |
| Pause/Resume | P |

---

## New Java Classes

### Controller Package (`com.comp2042.controller.*`)

| Class Name | Location | Purpose |
|------------|----------|---------|
| `GameController` | `controller.game` | Main game logic controller coordinating board operations |
| `GameEventHandler` | `controller.game` | Processes game events and coordinates mode-specific responses |
| `GameLoopManager` | `controller.game` | Manages the main game loop and piece dropping |
| `GameStateManager` | `controller.game` | Tracks game state (paused, game over) |
| `InputHandler` | `controller.input` | Handles all keyboard input with DAS/ARR support |
| `InputCoordinator` | `controller.input` | Coordinates input handling with game events |
| `InputEventListener` | `controller.input` | Interface for input event callbacks |
| `DASManager` | `controller.input` | Implements Delayed Auto Shift and Auto Repeat Rate |
| `GameInitializer` | `controller.lifecycle` | Initializes all game components |
| `GameLifecycleManager` | `controller.lifecycle` | Manages game lifecycle (pause, resume, new game) |
| `BlitzModeManager` | `controller.mode` | Handles Blitz mode specific logic (levels, timer) |
| `ZenModeManager` | `controller.mode` | Handles Zen mode specific logic (board clear tracking) |
| `MainMenuController` | `controller.menu` | Controls main menu interactions |

### Model Package (`com.comp2042.model.*`)

| Class Name | Location | Purpose |
|------------|----------|---------|
| `Board` | `model.game` | Interface defining board operations |
| `SimpleBoard` | `model.game` | Main board implementation with game logic |
| `BrickRotator` | `model.game` | Manages brick rotation states |
| `CollisionDetector` | `model.game` | Handles all collision detection logic |
| `HoldBrickManager` | `model.game` | Manages the hold brick feature |
| `Score` | `model.scoring` | Score tracking with persistent high scores |
| `BlitzLevel` | `model.mode` | Blitz mode level progression system |
| `ViewData` | `model.data` | Data transfer object for view updates |
| `DownData` | `model.data` | Data returned from down/drop events |
| `ClearRow` | `model.data` | Information about cleared rows |
| `MoveEvent` | `model.data` | Event data for piece movements |
| `NextShapeInfo` | `model.data` | Information about next brick rotation |

### View Package (`com.comp2042.view.*`)

| Class Name | Location | Purpose |
|------------|----------|---------|
| `GuiController` | `view.game` | Main GUI controller coordinating all views |
| `UIManager` | `view.game` | Manages UI elements (labels, panels) |
| `MenuNavigator` | `view.game` | Handles scene navigation |
| `BoardRenderer` | `view.rendering` | Renders the game board |
| `BrickRenderer` | `view.rendering` | Renders the active falling brick |
| `ShadowRenderer` | `view.rendering` | Renders the shadow/ghost piece |
| `PreviewRenderer` | `view.rendering` | Renders next/hold brick previews |
| `GameRendererCoordinator` | `view.rendering` | Coordinates all rendering operations |
| `GameOverPanel` | `view.components` | Custom game over display component |
| `NotificationPanel` | `view.components` | Score notification animations |

### Utility Package (`com.comp2042.util.*`)

| Class Name | Location | Purpose |
|------------|----------|---------|
| `GameConstants` | `util` | Centralized game configuration constants |
| `ColorPalette` | `util` | Centralized color management for bricks |
| `SoundManager` | `util` | Manages all game audio (music and SFX) |
| `TimerManager` | `util` | Manages Blitz countdown and Zen elapsed timers |
| `GameTimer` | `util` | Generic timer utility class |
| `MatrixOperations` | `util` | Matrix manipulation utilities (moved from root) |

### Logic Package (`com.comp2042.logic.bricks.*`)

| Class Name | Location | Purpose |
|------------|----------|---------|
| `Brick` | `logic.bricks` | Interface for brick shapes |
| `BrickGenerator` | `logic.bricks` | Interface for brick generation |
| `RandomBrickGenerator` | `logic.bricks` | 7-bag randomizer implementation |
| `IBrick` | `logic.bricks` | I-piece (cyan) implementation |
| `JBrick` | `logic.bricks` | J-piece (blue-violet) implementation |
| `LBrick` | `logic.bricks` | L-piece (green) implementation |
| `OBrick` | `logic.bricks` | O-piece (yellow) implementation |
| `SBrick` | `logic.bricks` | S-piece (red) implementation |
| `TBrick` | `logic.bricks` | T-piece (beige) implementation |
| `ZBrick` | `logic.bricks` | Z-piece (brown) implementation |

### Enums Package (`com.comp2042.enums.*`)

| Class Name | Location | Purpose |
|------------|----------|---------|
| `GameMode` | `enums` | Enum for game modes (ZEN, BLITZ) |
| `EventType` | `enums` | Enum for input event types |
| `EventSource` | `enums` | Enum for event sources (USER, THREAD) |

---

## Modified Java Classes

### Major Refactoring & Improvements

| Original Class | New Location/Name | Key Modifications | Rationale |
|----------------|-------------------|-------------------|-----------|
| `Main.java` | `com.comp2042.Main` | - Added main menu scene loading<br>- Implemented window scaling system<br>- Set proper window constraints | Better user experience with menu system and responsive design |
| `GuiController.java` | `com.comp2042.view.game.GuiController` | - Complete restructuring using component-based architecture<br>- Separated rendering, input, and lifecycle management<br>- Added support for multiple game modes | Improved maintainability and separation of concerns |
| `GameController.java` | `com.comp2042.controller.game.GameController` | - Added game mode parameter<br>- Integrated hold brick feature<br>- Added board clear detection for Zen mode<br>- Enhanced score calculation | Support for new features and game modes |
| `SimpleBoard.java` | `com.comp2042.model.game.SimpleBoard` | - Added game mode support<br>- Implemented hold brick system<br>- Added shadow position calculation<br>- Zen mode board clear logic<br>- Wall kick rotation support | Enhanced gameplay mechanics and mode-specific behavior |
| `BrickRotator.java` | `com.comp2042.model.game.BrickRotator` | - Added method to get current brick reference<br>- Improved encapsulation | Support for hold brick feature |
| `Score.java` | `com.comp2042.model.scoring.Score` | - Added persistent high score storage<br>- Game mode-specific high scores<br>- Observable properties for UI binding | Better score tracking and user experience |
| `MatrixOperations.java` | `com.comp2042.util.MatrixOperations` | - Made constructor private<br>- Added comprehensive documentation<br>- Fixed boundary check logic | Proper utility class pattern and bug fixes |
| `*Brick.java` classes | `com.comp2042.logic.bricks.*` | - Changed visibility to `public final`<br>- Updated imports for new package structure | Support for 7-bag randomizer and better encapsulation |
| `RandomBrickGenerator.java` | `com.comp2042.logic.bricks.RandomBrickGenerator` | - Implemented 7-bag algorithm<br>- Added reset functionality<br>- Replaced ThreadLocalRandom with Collections.shuffle | Fair piece distribution for competitive play |
| `ViewData.java` | `com.comp2042.model.data.ViewData` | - Added shadow Y position<br>- Added hold brick data<br>- Improved data encapsulation | Support for shadow piece and hold feature |
| `DownData.java` | `com.comp2042.model.data.DownData` | - Added board cleared flag<br>- Enhanced constructor | Support for Zen mode board clear detection |

### New Features Integration

| Class | Modification | Purpose |
|-------|--------------|---------|
| `gameLayout.fxml` | Complete redesign with new UI components | Modern UI with preview panels, timer, level display |
| `window_style.css` | Enhanced styling with Tokyo Night theme | Better visual aesthetics |
| Added `mainMenu.fxml` | New main menu layout | Game mode selection interface |

---

## Package Structure Changes

### Before (Original Structure)
```
com.comp2042
├── Main.java
├── GuiController.java
├── GameController.java
├── SimpleBoard.java
├── Board.java
├── Score.java
├── BrickRotator.java
├── MatrixOperations.java
├── ViewData.java
├── DownData.java
├── ClearRow.java
├── MoveEvent.java
├── EventType.java
├── EventSource.java
├── NextShapeInfo.java
├── InputEventListener.java
├── GameOverPanel.java
├── NotificationPanel.java
└── logic.bricks
    ├── Brick.java
    ├── BrickGenerator.java
    ├── RandomBrickGenerator.java
    └── [7 Brick implementations]
```

### After (Refactored Structure)
```
com.comp2042
├── Main.java
├── controller
│   ├── game
│   │   ├── GameController.java
│   │   ├── GameEventHandler.java
│   │   ├── GameLoopManager.java
│   │   └── GameStateManager.java
│   ├── input
│   │   ├── InputHandler.java
│   │   ├── InputCoordinator.java
│   │   ├── InputEventListener.java
│   │   └── DASManager.java
│   ├── lifecycle
│   │   ├── GameInitializer.java
│   │   └── GameLifecycleManager.java
│   ├── mode
│   │   ├── BlitzModeManager.java
│   │   └── ZenModeManager.java
│   └── menu
│       └── MainMenuController.java
├── model
│   ├── game
│   │   ├── Board.java
│   │   ├── SimpleBoard.java
│   │   ├── BrickRotator.java
│   │   ├── CollisionDetector.java
│   │   └── HoldBrickManager.java
│   ├── scoring
│   │   └── Score.java
│   ├── mode
│   │   └── BlitzLevel.java
│   └── data
│       ├── ViewData.java
│       ├── DownData.java
│       ├── ClearRow.java
│       ├── MoveEvent.java
│       └── NextShapeInfo.java
├── view
│   ├── game
│   │   ├── GuiController.java
│   │   ├── UIManager.java
│   │   └── MenuNavigator.java
│   ├── rendering
│   │   ├── BoardRenderer.java
│   │   ├── BrickRenderer.java
│   │   ├── ShadowRenderer.java
│   │   ├── PreviewRenderer.java
│   │   └── GameRendererCoordinator.java
│   └── components
│       ├── GameOverPanel.java
│       └── NotificationPanel.java
├── logic
│   └── bricks
│       ├── Brick.java
│       ├── BrickGenerator.java
│       ├── RandomBrickGenerator.java
│       └── [7 Brick implementations]
├── util
│   ├── GameConstants.java
│   ├── ColorPalette.java
│   ├── SoundManager.java
│   ├── TimerManager.java
│   ├── GameTimer.java
│   └── MatrixOperations.java
└── enums
    ├── GameMode.java
    ├── EventType.java
    └── EventSource.java
```

---

## Design Patterns Applied

| Pattern | Implementation | Benefit |
|---------|---------------|---------|
| **MVC (Model-View-Controller)** | Separated game logic (Model), UI rendering (View), and control flow (Controller) | Clear separation of concerns, easier testing |
| **Observer Pattern** | Used JavaFX Properties for score, timer, and level updates | Automatic UI updates when data changes |
| **Strategy Pattern** | Different game modes (Zen/Blitz) with mode-specific managers | Easy to add new game modes |
| **Singleton Pattern** | SoundManager instance | Single audio management point |
| **Factory Pattern** | BrickGenerator interface with RandomBrickGenerator | Flexible brick generation strategies |
| **Coordinator Pattern** | GameRendererCoordinator, InputCoordinator | Simplified component orchestration |
| **State Pattern** | GameStateManager for pause/game over states | Clean state management |

---

## Key Technical Improvements

### 1. Input System Enhancement

- **DAS (Delayed Auto Shift):** 170ms delay before repeat starts
- **ARR (Auto Repeat Rate):** 50ms between repeated inputs
- **Result:** Smooth, responsive controls matching modern Tetris standards

### 2. Rendering Architecture

- Separated rendering concerns into specialized classes
- Double-buffered rendering prevents flickering
- Shadow piece rendered separately from active piece

### 3. Sound Management

- Centralized audio control with SoundManager
- Proper cleanup and resource management

### 4. Score Persistence

- Uses Java Preferences API for cross-platform storage
- Separate high scores for each game mode
- Automatic save on new high score

### 5. Collision Detection

- Extracted into dedicated CollisionDetector class
- Wall kick algorithm for rotation near boundaries
- Shadow position calculation for ghost piece

### 6. Fair Randomization

- Implemented 7-bag system
- Ensures no piece drought (e.g., no I-piece for 13+ pieces)
- Maintains two bags ahead for smooth generation

---

## Unexpected Problems & Solutions

### Problem 1: Timeline Memory Leaks

**Issue:** Multiple Timeline objects not properly cleaned up when returning to menu, causing audio to continue playing and memory leaks.

**Solution:**
- Created comprehensive cleanup system in GameLifecycleManager
- Added cleanup() methods to all timer classes
- Implemented setOnReturnToMenuCleanup callback pattern
- Proper Timeline stop and null assignment

**Code Location:** `GameLifecycleManager.java`, `TimerManager.java`, `MenuNavigator.java`

---

### Problem 2: Window Scaling Distortion

**Issue:** Game content stretched incorrectly when window resized, breaking aspect ratio.

**Solution:**
- Implemented StackPane-based scaling system
- Calculated uniform scale factor based on smaller dimension
- Applied scale transformation while maintaining preferred size
- Set minimum window constraints

**Code Location:** `Main.java` (lines 47-71), `MainMenuController.java` (lines 142-157)

---

### Problem 3: DAS/ARR Conflicts

**Issue:** Multiple key presses caused conflicting timelines, resulting in erratic movement.

**Solution:**
- Created dedicated DASManager to track pressed keys
- Stopped existing timelines before starting new ones
- Implemented proper key release handling
- Used HashSet to track currently pressed keys

**Code Location:** `DASManager.java`, `InputHandler.java`

---

### Problem 4: Shadow Piece Flicker

**Issue:** Shadow piece flickered when piece moved due to recreation every frame.

**Solution:**
- Reuse Rectangle objects when brick size doesn't change
- Clear and re-add only when necessary
- Check for size changes before recreation
- Set to transparent when not needed instead of removing

**Code Location:** `ShadowRenderer.java` (lines 54-89)

---

### Problem 5: Hold Feature Game Over Detection

**Issue:** Swapping held piece could place piece in invalid position, causing immediate game over.

**Solution:**
- Check collision after swapping held piece
- Attempt to move piece upward if collision detected
- Revert swap if no valid position found
- Disable hold temporarily after successful swap

**Code Location:** `SimpleBoard.java` holdBrick() method (lines 163-197)

---

### Problem 6: Blitz Timer Not Stopping on Game Over

**Issue:** Blitz countdown continued after game over, potentially triggering multiple game over events.

**Solution:**
- Added isCleanedUp flag to TimerManager
- Check flag before executing time-up callback
- Proper cleanup order in lifecycle manager
- Guard clauses in all timer methods

**Code Location:** `TimerManager.java` (lines 28, 47, 103-112)

---

### Problem 7: Sound Resource Loading

**Issue:** Audio files not found when packaged in JAR due to incorrect resource path handling.

**Solution:**
- Used getResourceAsStream() instead of File operations
- Added BufferedInputStream for better performance
- Proper error handling and logging
- Verified resource paths in Maven structure

**Code Location:** `SoundManager.java` (lines 40-120)

---

## Testing Approach

### Manual Testing Performed

- All key bindings (movement, rotation, hard drop, hold, pause)
- Both game modes (Zen and Blitz) through complete gameplay
- Score persistence across application restarts
- Window resizing and scaling
- Audio playback and transitions
- Edge cases (rapid key presses, simultaneous inputs)
- Menu navigation (game → menu → game)
- Pause and resume functionality
- Game over conditions
- Hold feature limitations (one hold per piece placement)

### Known Edge Cases Handled

- Holding piece when at top of board
- Rotating near walls
- Rapid piece placement
- Simultaneous key presses
- Window minimize/maximize
- Audio system when files missing

---

## Future Enhancement Opportunities

1. **Online Leaderboards:** Integration with backend service for global rankings
2. **Custom Themes:** User-selectable color schemes and backgrounds
3. **Replay System:** Record and playback games
4. **Advanced Statistics:** Track T-spins, perfect clears, etc.
5. **Mobile Version:** Touch controls and portrait orientation
6. **Tournament Mode:** Head-to-head competition features
7. **Custom Key Bindings:** User-configurable controls
8. **Training Mode:** Practice specific scenarios

---

## Conclusion

This project successfully transformed a basic Tetris implementation into a feature-rich, modern game with two distinct game modes, advanced input handling, and professional polish. The refactoring effort focused on:

- **Maintainability:** Clear package structure and separation of concerns
- **Extensibility:** Easy to add new game modes and features
- **User Experience:** Smooth controls, visual feedback, persistent scores
- **Code Quality:** Design patterns, documentation, proper resource management

The final product demonstrates industry-standard practices for game development in Java, with particular attention to architecture, user experience, and code maintainability.

### Project Statistics

- **Total New Classes:** 45
- **Total Modified Classes:** 15
- **Lines of Code Added:** ~4,500

---

**Documentation generated by WONG YUNG TUNG**  
**Course:** COMP2042  
**Date:** 4/12/2025
