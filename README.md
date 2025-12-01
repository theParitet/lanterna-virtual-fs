# Lanterna Virtual File System

<img width="600" src="https://github.com/theParitet/lanterna-virtual-fs/blob/main/docs/assets/cli-demo1.gif?raw=true" alt="CLI terminal window running the Lanterna application." />

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


<p>
  <img width="300" src="https://github.com/theParitet/lanterna-virtual-fs/blob/main/docs/assets/swing-demo1.png?raw=true" alt="Retro gray file manager with a split-pane interface in a Swing window." />
  <img width="300" src="https://github.com/theParitet/lanterna-virtual-fs/blob/main/docs/assets/swing-demo2.png?raw=true" alt="Blue-themed file manager TUI in a Swing window showing a file creation dialog." />
</p>
<img width="600" src="https://github.com/theParitet/lanterna-virtual-fs/blob/main/docs/assets/swing-demo3.png?raw=true" alt="Matrix-styled file editor displaying JSX editting in a Swing window." />

## Tech

- Java (21)
- Apache Maven – build tool
- Maven Wrapper – for consistent, reproducible builds
- Maven Shade Plugin – for creating fat/uber `.jar` executables
- [Lanterna](https://github.com/mabe02/lanterna) (3.1+) – TUI library, the base of the project 

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
