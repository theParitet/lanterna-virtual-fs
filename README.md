# Lanterna Virtual File System

...

This is a virtual file system implemented with a TUI built on the Lanterna API.

It’s a small educational project that simulates a file system for demonstration and experimentation.

Currently implemented features:
- File and folder operations – create, rename, delete
- Live content preview updating upon selection changes
- Dynamic path display that adapts to directory changes and terminal width
- Traversal and recursive search
- A set of themes altering both colors and shapes

Notable:
- Responsive TUI under typical workloads
- Startup configuration phase
- Info and help pages

...

## Run

To run the application build:

`java -jar LanternaVFS.jar`

### Compatibility

The application runs well on the CLI of macOS and Linux systems.

Lanterna’s Windows backend has incomplete support for modern terminal features. Cannot be run in WT, CMD and PowerShell in its current implementation. Due to this limited compatibility, the application 
will **fall back to a Swing app on Windows**, instead of running on command line. 

Limited terminal color and character encoding UTF-8 support are the root of many UI issues. Use terminal with UTF-8 and rich color support.

## Build

To build the application:

`./mvnw clean package`

The executables are placed in `target` directory:

- `original-LanternaVFS.jar` – thin executable
- `LanternaVFS.jar` – shaded (fat) executable

## Issues & Troubleshooting

- Path width not updating on resize – the path label refreshes only upon changing directories.
- Inconsistent or broken UI – UI artifacts may be present when changing themes, terminal resizing or through other app interactions. 
  They are usually resolved after interacting with the broken UI elements (causing a redraw).
  Small terminal dimensions and very long filenames/content often trigger this. Follow the configuration step and 
  avoid large content .
- Color inconsistency or invisible characters – may occur on terminals with weird palettes or incomplete Unicode support. Switching themes or 
  using a different terminal often resolve it.
- Vim keys – experimental and limited to `h/j/k/l` within `mainWindow` UI.

Bug reports and contribution is welcome in this GitHub repo.

## Implementation

The virtual file system is represented as an n-ary tree with `~` as the root node. Each node stores basic metadata and its content (file text or 
the folder's children).
Current implementation keeps most of the system functionality and UI definitions within `main` method, which is to be changed in future iterations.