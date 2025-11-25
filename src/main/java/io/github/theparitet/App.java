package io.github.theparitet;

/**
 * This file is part of LanternaVFS project (https://github.com/theParitet/lanterna-virtual-fs).
 * Check out README.md and LICENSE for more information.
 */

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.bundle.LanternaThemes;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.*;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class App {

    public static FolderNode root = new FolderNode("~", null);
    public static FolderNode currentFolder = root;
    public static Label pathLabel;
    public static Label previewLabel;

    public static WindowBasedTextGUI textGUI;
    public static ActionListBox filesList;
    public static Window mainWindow;

    public static void main(String[] args) {
        DefaultTerminalFactory dtf = new DefaultTerminalFactory();
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        if (isWindows) {
            dtf.setPreferTerminalEmulator(true);
            dtf.setTerminalEmulatorTitle("Lanterna VFS (Windows Compatibility Mode)");
        } else {
            dtf.setPreferTerminalEmulator(false);
        }

        Screen screen = null;

//        SAMPLE
        FolderNode folder1 = new FolderNode("Pictures", root);

        FolderNode nested11 = new FolderNode("Landscape");
        FileNode nested111 = new FileNode("mountains.png");
        FileNode nested112 = new FileNode("sunset.jpg");
        nested11.addChild(nested111);
        nested11.addChild(nested112);
        FolderNode nested12 = new FolderNode("Puppies");
        FileNode nested121 = new FileNode("puppy.png");
        FileNode nested122 = new FileNode("puppy2.jpg");
        nested12.addChild(nested121);
        nested12.addChild(nested122);
        FolderNode nested13 = new FolderNode("Travel");
        FileNode nested131 = new FileNode("paris.png");
        FileNode nested132 = new FileNode("tokyo.jpg");
        nested13.addChild(nested131);
        nested13.addChild(nested132);
        folder1.addChild(nested11);
        folder1.addChild(nested12);
        folder1.addChild(nested13);

        FolderNode folder2 = new FolderNode("Games", root);

        FolderNode nested21 = new FolderNode("Shooters");
        FileNode nested22 = new FileNode("WarThunder.exe");
        FileNode nested23 = new FileNode("CallOfDuty.exe");
        nested21.addChild(nested22);
        nested21.addChild(nested23);
        FolderNode nested24 = new FolderNode("RPG");
        FileNode nested25 = new FileNode("Skyrim.exe");
        FileNode nested26 = new FileNode("Witcher3.exe");
        FileNode nested27 = new FileNode("savegame1.sav");
        FileNode nested28 = new FileNode("savegame2.sav");
        nested24.addChild(nested25);
        nested24.addChild(nested26);
        nested24.addChild(nested27);
        nested24.addChild(nested28);
        FolderNode nested29 = new FolderNode("Indie");
        FileNode nested210 = new FileNode("HollowKnight.exe");
        FileNode nested211 = new FileNode("StardewValley.exe");
        FileNode nested212 = new FileNode("notes.txt"); // maybe a random modding note
        nested29.addChild(nested210);
        nested29.addChild(nested211);
        nested29.addChild(nested212);
        FolderNode nested213 = new FolderNode("Retro");
        FileNode nested214 = new FileNode("PacMan.exe");
        FileNode nested215 = new FileNode("Tetris.exe");
        nested213.addChild(nested214);
        nested213.addChild(nested215);
        folder2.addChild(nested21);
        folder2.addChild(nested24);
        folder2.addChild(nested29);
        folder2.addChild(nested213);

        FolderNode folder3 = new FolderNode("Programming", root);

        FolderNode lanternaProject = new FolderNode("LanternaVFS", folder3);
        FolderNode lanternaSrc = new FolderNode("src");
        FileNode lanternaMain = new FileNode("App.java");
        FileNode lanternaUtils = new FileNode("Node.java");
        FileNode lanternaPom = new FileNode("pom.xml");
        FileNode lanternaReadme = new FileNode("README.md");
        lanternaSrc.addChild(lanternaMain);
        lanternaSrc.addChild(lanternaUtils);
        lanternaProject.addChild(lanternaSrc);
        lanternaProject.addChild(lanternaPom);
        lanternaProject.addChild(lanternaReadme);
        FolderNode webProject = new FolderNode("web", folder3);
        FolderNode webSrc = new FolderNode("src");
        FileNode webIndex = new FileNode("index.html");
        FileNode webAppJS = new FileNode("App.jsx");
        FileNode webStyle = new FileNode("style.css");
        webSrc.addChild(webIndex);
        webSrc.addChild(webAppJS);
        webSrc.addChild(webStyle);
        webProject.addChild(webSrc);
        webProject.addChild(new FileNode("package.json"));
        webProject.addChild(new FileNode("README.md"));
        FolderNode pythonProject = new FolderNode("py scripts", folder3);
        FileNode script1 = new FileNode("data_analysis.py");
        FileNode script2 = new FileNode("plot_graphs.py");
        pythonProject.addChild(script1);
        pythonProject.addChild(script2);
        pythonProject.addChild(new FileNode("requirements.txt"));
        folder3.addChild(lanternaProject);
        folder3.addChild(webProject);
        folder3.addChild(pythonProject);

        root.addChild(folder1);
        root.addChild(folder2);
        root.addChild(folder3);
        root.addChild(new FileNode("report.txt", root));
        root.addChild(new FileNode("charts.xls", root));
        root.addChild(new FileNode("app.jar", root));
        root.addChild(new FileNode("screenshot2025.png", root));
        root.addChild(new FileNode("PHY201 formulae.pdf", root));
        root.addChild(new FileNode("passwords.txt", root));
//        SAMPLE


        try {
//            setup
            screen = dtf.createScreen();
            screen.startScreen();

            textGUI = new MultiWindowTextGUI(screen);
            mainWindow = new BasicWindow("File Manager Emulator");

            final Panel panel = new Panel(new GridLayout(3));

            Random rand = new Random();

            if (isWindows) {
                notify("Windows Compatibility", "" +
                        "Although the application runs well on UNIX systems,\n" +
                        "it doesn't run well on Windows.\n" +
                        "Hence, desktop frontend will be used,\n" +
                        "instead of running it from the command line.");
            }

//            window size configuration
            Label termSize = new Label(screen.getTerminalSize().toString());
            Screen finalScreen = screen;
            Thread tt = new Thread(() -> {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    try {
                        Thread.sleep(50);
                        TerminalSize ts = finalScreen.getTerminalSize();

                        boolean enoughColumns = ts.getColumns() >= 90;
                        boolean enoughRows = ts.getRows() >= 30;
                        sb.append(ts).append("\n");

                        sb.append("[").append(enoughColumns ? "x":"_").append("] – Columns\n");
                        sb.append("[").append(enoughRows ? "x":"_").append("] – Rows\n");
                        if (enoughColumns && enoughRows) {
                            sb.append("Perfect! :D");
                        } else if (enoughColumns || enoughRows) {
                            sb.append("Almost there.");
                        } else {
                            sb.append("Not recommended.");
                        }

                        String finalCurrentTerm = sb.toString();
                        textGUI.getGUIThread().invokeLater(() -> {
                            termSize.setText(finalCurrentTerm);
                        });
                        sb.setLength(0);
                    } catch (InterruptedException ignore) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            });
            tt.start();

            Window ww = new BasicWindow("Window size");
//            ww.setHints(Collections.singleton(Window.Hint.CENTERED));

            Panel pp = new Panel(new LinearLayout(Direction.VERTICAL));

            pp.addComponent(new Label("Recommended window dimensions are 90x30.\nTry adjusting the window to meet these values."));
            pp.addComponent(new EmptySpace());
            pp.addComponent(termSize);
            pp.addComponent(new EmptySpace());
            pp.addComponent(new Button("Continue", () -> {
                tt.interrupt();
                ww.close();
            }).setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));

            ww.setComponent(pp);
            textGUI.addWindowAndWait(ww);

//            menu bar
            MenuBar mb = new MenuBar();
            Menu system = new Menu("System")
                    .add(new MenuItem("About", () -> {
                        notify("About", "" +
                                "Virtual TUI-based file system.\n" +
                                "Inspired by nnn package manager and its plugins.\n" +
                                "You can:\n" +
                                "- create files and directories\n" +
                                "- preview and edit content\n" +
                                "- traverse the system\n" +
                                "- change themes\n");
                    }))
                    .add(new MenuItem("Technical Info", () -> {
                        notify("Technical Info", "" +
                                "Version:                                  0.1\n" +
                                "Programming Language:               Java (21)\n" +
                                "Package Manger:                         Maven\n" +
                                "Libraries:             Lanterna (TUI library)\n" +
                                "Coffee consumed:                  25.3 litres\n" +
                                "\n" +
                                "File system is implemented using n-ary trees,\nwhere each file and folder is a node.");
                    }))
                    .add(new MenuItem("Exit", () -> {
                        Window w = new BasicWindow();
                        w.setHints(Collections.singleton(Window.Hint.CENTERED));

                        Panel p = new Panel(new LinearLayout(Direction.VERTICAL));

                        p.addComponent(new Label("Do not close this window")
                                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));
                        p.addComponent(new Label("(imagine it is saving something)")
                                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));
                        p.addComponent(new EmptySpace());
                        p.addComponent(new Label("Quitting...")
                                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));

                        ProgressBar pg = new ProgressBar(0, 100, 40);
                        p.addComponent(pg);
                        p.addComponent(new EmptySpace());
                        p.addComponent(new Button("Abort", () -> {
                            textGUI.setActiveWindow(mainWindow);
                            w.close();
                        }).setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));


                        w.setComponent(p);
                        textGUI.addWindow(w);
                        textGUI.setActiveWindow(w);

                        Thread workerThread = new Thread(() -> {
                            try {
                                int value = 0;
                                while (value < 100) {
                                    Thread.sleep(rand.nextInt(100,500));
                                    value += rand.nextInt(10);
                                    final int pValue = value;
                                    textGUI.getGUIThread().invokeLater(() -> {
                                        pg.setValue(Math.min(100, pValue));
                                    });
                                }
                                if (textGUI.getActiveWindow().equals(w)) {
                                    mainWindow.close();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        workerThread.start();
                    }));
            Menu view = new Menu("View")
                    .add(new MenuItem("Traverse Full Tree", () -> {
                        ArrayList<Node> flatTree = new ArrayList<>();
                        String s = renderTree(root, flatTree);
                        String[] lines = s.split("\n");
                        ActionListDialogBuilder aldb = new ActionListDialogBuilder()
                                .setTitle("File Tree")
                                .setListBoxSize(new TerminalSize(mainWindow.getSize().getColumns(), mainWindow.getSize().getRows()-2));
                        Iterator<String> lineIterator = List.of(lines).iterator();
                        flatTree.forEach(n -> {
                            aldb.addAction(lineIterator.next(),() -> {
                                nodeAction(n);
                            });
                        });

                        aldb.build().showDialog(textGUI);
                    }));
            Menu help = new Menu("Help")
                    .add(new MenuItem("Controls", () -> showHelp()))
                    .add(new MenuItem("Technical Issues", () -> {
                        notify("Technical Issues", "" +
                                "Inconsistent UI\n" +
                                "    Sticking to the recommended window dimensions of\n" +
                                "    at least 90x30 characters could improve UI flow.\n" +
                                "\n" +
                                "Windows compatibility\n" +
                                "    Although the application runs relatively well\n" +
                                "    on the Linux and macOS CLI, it may have issues\n" +
                                "    doing so on Windows CLI. That is why the desktop\n" +
                                "    application view is enforced for Windows users.\n" +
                                "    As a macOS user, I can neither test nor run it.\n" +
                                "    If you wish to contribute, follow the link below.\n" +
                                "\n" +
                                "You can also submit your issue using this github link:\n" +
                                "https://github.com/theParitet/lanterna-virtual-fs/issues");
                    }));

            Button i = new Button("(i)", () -> {
                notify("Paths Info", "" +
                        "As per UNIX file systems:\n" +
                        ".  – current directory\n" +
                        ".. – parent directory\n" +
                        "~  – home directory\n" +
                        "~ is considered root in this system");
            });
            i.setPreferredSize(new TerminalSize(5, 1));

            Menu themes = new Menu("Themes")
                    .add(new MenuItem("Default", () -> {
                        textGUI.setTheme(LanternaThemes.getRegisteredTheme("default"));
                        i.setPreferredSize(new TerminalSize(5, 1));
                    }))
                    .add(new MenuItem("Blaster", () -> {
                        textGUI.setTheme(LanternaThemes.getRegisteredTheme("blaster"));
                        i.setPreferredSize(new TerminalSize(3, 1));
                    }))
                    .add(new MenuItem("Business Machine", () -> {
                        textGUI.setTheme(LanternaThemes.getRegisteredTheme("businessmachine"));
                        i.setPreferredSize(new TerminalSize(5, 1));
                    }))
                    .add(new MenuItem("Conqueror", () -> {
                        textGUI.setTheme(LanternaThemes.getRegisteredTheme("conqueror"));
                        i.setPreferredSize(new TerminalSize(5, 1));
                    }))
                    .add(new MenuItem("Defrost", () -> {
                        textGUI.setTheme(LanternaThemes.getRegisteredTheme("defrost"));
                        i.setPreferredSize(new TerminalSize(5, 1));
                    }))
                    .add(new MenuItem("Big Snake", () -> {
                        textGUI.setTheme(LanternaThemes.getRegisteredTheme("bigsnake"));
                        i.setPreferredSize(new TerminalSize(8, 4));
                    }));

            mb
                    .add(system)
                    .add(themes)
                    .add(view)
                    .add(help);

            panel.addComponent(mb
                    .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(3)));

//            path box
            pathLabel = new Label(getAdjustedPath(root));
            pathLabel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
            Panel pathBox = getFlexHor();
            pathBox
                    .addComponent(i)
                    .addComponent(pathLabel.addStyle(SGR.BOLD))
                    .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2));
            panel.addComponent(pathBox.withBorder(Borders.singleLineReverseBevel("Path")));

            Panel searchBox = new Panel(new LinearLayout(Direction.HORIZONTAL).setSpacing(1));
            TextBox searchText = new TextBox();
            Button searchBtn = new Button("Search", () -> {
                if (searchText.getText() != null && !searchText.getText().strip().isBlank()) {
                    String toSearch = searchText.getText().strip();
                    List<Node> found = searchRecursively(toSearch);
                    if (!found.isEmpty()) {
                        ActionListDialogBuilder aldb = new ActionListDialogBuilder()
                                .setTitle("Search Matches")
                                .setListBoxSize(new TerminalSize(mainWindow.getSize().getColumns(), mainWindow.getSize().getRows()-2));
                        found.forEach(node -> {
                            aldb.addAction(node.getName(), () -> {
                                nodeAction(node);
                            });
                        });
                        aldb.build().showDialog(textGUI);
                    } else {
                        notify("No matches", "There are no files or folders in the system with such name.");
                    }
                } else {
                    notify("Invalid input", "The search input field is empty.");
                }
                searchText.setText("");
            });

            searchText.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Beginning, LinearLayout.GrowPolicy.CanGrow));
            searchBtn.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.End));

            panel.addComponent(searchBox
                    .addComponent(searchText)
                    .addComponent(searchBtn)
                    .withBorder(Borders.singleLineReverseBevel())
                    .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1)));

            panel.addComponent(new Separator(Direction.HORIZONTAL).setLayoutData(GridLayout.createHorizontallyFilledLayoutData(3)));

//            file panel
            filesList = new ActionListBox();
            renderChildren();

            panel.addComponent(getFlexHor()
                    .addComponent(new Button("Add Folder", () -> {
                        String nodeName = new TextInputDialogBuilder()
                                .setTitle("Folder creation")
                                .setDescription("\nEnter the folder name:")
                                .build()
                                .showDialog(textGUI);
                        if (nodeName != null) {
                            nodeName = nodeName.strip();
                            if (!nodeName.isBlank()) {
                                boolean detectedCollision = !currentFolder.addChild(new FolderNode(nodeName, currentFolder));
                                if (detectedCollision) {
                                    notify("Invalid Name – Collision", "File/Folder with such name already exists. Use unique names.");
                                } else {
                                    renderChildren();
                                }
                            } else {
                                notify("Invalid Name – Blank", "Name cannot be blank.");
                            }
                        }
                    }))
                    .addComponent(new Button("Add File", () -> {
                        String nodeName = new TextInputDialogBuilder()
                                .setTitle("File creation")
                                .setDescription("\nEnter the file name:")
                                .build()
                                .showDialog(textGUI);
                        if (nodeName != null) {
                            nodeName = nodeName.strip();
                            if (!nodeName.isBlank()) {
                                boolean detectedCollision = !currentFolder.addChild(new FileNode(nodeName, currentFolder));
                                if (detectedCollision) {
                                    notify("Invalid Name – Collision", "File/Folder with such name already exists. Use unique names.");
                                } else {
                                    renderChildren();
                                }
                            } else {
                                notify("Invalid Name – Blank", "Name cannot be blank.");
                            }
                        }
                    })));

            Thread worker = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException ignore) {}
                    textGUI.getGUIThread().invokeLater(() -> {
                        if (filesList.isFocused()) {
                            SGR empty = SGR.REVERSE;
                            previewLabel.removeStyle(empty);
                            int index = filesList.getSelectedIndex();
                            if (currentFolder != root) index--;
                            if (currentFolder != root && filesList.getSelectedIndex() == 0) {
                                previewLabel.addStyle(SGR.BOLD);
                                String p = getPreview(currentFolder).strip();
                                if (p.isBlank()) {
                                    previewLabel.setText("<EMPTY>");
                                    previewLabel.addStyle(empty);
                                    previewLabel.addStyle(SGR.BOLD);
                                } else {
                                    previewLabel.setText(p);
                                }
                            }
                            if (index != -1) {
                                Node item = currentFolder.get(index);
                                if (item instanceof FolderNode) {
                                    previewLabel.addStyle(SGR.BOLD);
                                } else {
                                    previewLabel.removeStyle(SGR.BOLD);
                                }

                                String p = getPreview(item);
                                if (p.isBlank()) {
                                    previewLabel.setText("<EMPTY>");
                                    previewLabel.addStyle(empty);
                                    previewLabel.addStyle(SGR.BOLD);
                                } else {
                                    previewLabel.setText(p);
                                }
                            }
                        }
                    });
                }
            });

            worker.start(); // suboptimal, temporary solution

            panel.addComponent(new Label("Live Preview")
                    .setLayoutData(GridLayout
                            .createLayoutData(
                                    GridLayout.Alignment.CENTER,
                                    GridLayout.Alignment.CENTER,
                                    true,
                                    false,
                                    2,
                                    1))
                    .addStyle(SGR.BOLD));

//            open controls help page by pressing ? or /
            mainWindow.addWindowListener(new WindowListener() {
                @Override public void onInput(Window window, KeyStroke keyStroke, AtomicBoolean atomicBoolean) {
                    if (keyStroke.getCharacter() != null && (keyStroke.getCharacter().equals('?') || keyStroke.getCharacter().equals('/'))) {
                        showHelp();
                        atomicBoolean.set(true);
                    }
                }
                @Override public void onResized(Window window, TerminalSize terminalSize, TerminalSize terminalSize1) { }
                @Override public void onMoved(Window window, TerminalPosition terminalPosition, TerminalPosition terminalPosition1) { }
                @Override public void onUnhandledInput(Window window, KeyStroke keyStroke, AtomicBoolean atomicBoolean) { }
            });

//            adding vim bindings
            mainWindow.addWindowListener(new VimNavigation());

//            listening keystrokes for D/backspace to delete and for R to rename
            filesList.setInputFilter((interactable, keyStroke) -> {
                if (filesList.isFocused()) {
                    int index;
                    Node item;
                    index = filesList.getSelectedIndex();
                    int realIndex = index;
                    if (currentFolder != root) index--;
                    if (index != -1) {
                        item = currentFolder.get(index);
                        String nodeType = item instanceof FolderNode ? "folder":"file";
                        if (keyStroke.getKeyType().equals(KeyType.Backspace) ||
                                keyStroke.getCharacter() != null && keyStroke.getCharacter().equals('d')) {
                            Panel modal = new Panel(new GridLayout(2));

                            Window w = new BasicWindow();
                            w.setComponent(modal);
                            w.setHints(Collections.singleton(Window.Hint.CENTERED));

                            modal.addComponent(new Label("Are you sure you want to delete " +
                                    item.getName() + " " + nodeType + "?")
                                    .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2)));
                            modal.addComponent(new EmptySpace().setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2)));
                            modal.addComponent(new Button("Cancel", () -> {
                                textGUI.setActiveWindow(mainWindow);
                                w.close();
                            }).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));
                            modal.addComponent(new Button("Delete", () -> {
                                FolderNode parent = (FolderNode)item.getParent();
                                parent.removeChild(item);
                                renderChildren();

                                textGUI.setActiveWindow(mainWindow);
                                w.close();

                                notify(item.getName(), "File deleted.");
                            }).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));

                            textGUI.addWindow(w);
                            textGUI.setActiveWindow(w);
                            return false;
                        }
                        if (keyStroke.getCharacter() != null && keyStroke.getCharacter().equals('r')) {
                            String nodeName = new TextInputDialogBuilder()
                                    .setTitle("Enter the new name for " + item.getName() + " " + nodeType + ":")
                                    .setDescription("")
                                    .setInitialContent(item.getName())
                                    .build()
                                    .showDialog(textGUI);
                            if (nodeName != null) {
                                if (!nodeName.strip().isBlank()) {
                                    item.setName(nodeName);
                                    renderChildren();
                                    filesList.setSelectedIndex(realIndex);
                                } else {
                                    notify("Invalid Name – Blank", "Name cannot be blank.");
                                }
                            }
                            return false;
                        }
                    }
                }
                return true;
            });

            panel.addComponent(filesList.setPreferredSize(new TerminalSize(25, 10)));

            previewLabel = new Label(" ");
            panel.addComponent(previewLabel
                    .setLayoutData(GridLayout.createLayoutData(
                            GridLayout.Alignment.FILL,
                            GridLayout.Alignment.FILL,
                            false,
                            false,
                            2,
                            1
                    )).setLabelWidth(25));

            mainWindow.setComponent(panel);
            mainWindow.setHints(Collections.singleton(Window.Hint.EXPANDED));
            textGUI.addWindowAndWait(mainWindow);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (screen != null) {
                try {
                    screen.stopScreen();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.exit(1);
    }

    public static void notify(String title, String description) {
        new MessageDialogBuilder()
                .setTitle(title)
                .setText("\n"+description)
                .addButton(MessageDialogButton.OK)
                .build()
                .showDialog(textGUI);
    }

    public static void showHelp() {
        notify("Controls", "" +
                "    Main controls\n" +
                "<r> .................. to rename files\n" +
                "<d> or <BACKSPACE> ... to delete files\n" +
                "arrow keys and <TAB> . to traverse GUI\n" +
                "\n    VIM\n" +
                "<h/j/k/l> ... experimental VIM support\n" +
                "VIM is lacking normal and insert modes\n" +
                "Traversal only through main GUI window\n" +
                "\n" +
                "    <?> or </> – to open this page");
    }

    public static void renderChildren() {
        List<Node> children = currentFolder.getChildren();

        filesList.clearItems();

        if (!currentFolder.equals(root)) {
            filesList.addItem("..", () -> {
                nodeAction(currentFolder.parent);
            });
        }

        children.sort((o1, o2) -> {
                if (o1 instanceof FolderNode && o2 instanceof FileNode) return -1;
                if (o1 instanceof FileNode && o2 instanceof FolderNode) return 1;
                return 0;
        });

        children.forEach(child -> {
            filesList.addItem((child instanceof FolderNode ? "[F] " : "<f> ") + child.getName(), () -> {
                nodeAction(child);
            });
        });
    }

    public static void nodeAction(Node node) {
        switch (node) {
//            change current directory
            case FolderNode fn -> {
                Thread worker = new Thread(() -> {
                    textGUI.getGUIThread().invokeLater(() -> {
                        currentFolder = fn;

                        String path = getAdjustedPath(currentFolder);
                        pathLabel.setText(path);

                        renderChildren();
                    });
                });
                worker.start();
            }
//            change the content of the file and cd into the file directory
            case FileNode fn -> {
                String result = new TextInputDialogBuilder()
                        .setTitle(fn.getName())
                        .setExtraWindowHints(Collections.singleton(Window.Hint.EXPANDED))
                        .setTextBoxSize(mainWindow.getSize())
                        .setInitialContent(fn.getContent())
                        .build()
                        .showDialog(textGUI);
                if (result != null) {
                    fn.setContent(result);
                    nodeAction(fn.parent);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + node);
        }
    }

    public static String getPreview(Node node) {
        switch (node) {
            case FolderNode fn -> {
                StringBuilder sb = new StringBuilder();
                for (Node n : fn.getChildren()) {
                    sb.append((n instanceof FolderNode ? "[F] " : "<f> ") + n.getName()).append("\n");
                }
                return sb.toString();
            }
            case FileNode fn -> {
                return fn.getContent();
            }
            default -> throw new IllegalStateException("Unexpected value: " + node);
        }
    }

    public static Panel getFlexHor() {
        return new Panel(new LinearLayout(Direction.HORIZONTAL));
    }
    public static Panel getFlexVer() {
        return new Panel(new LinearLayout(Direction.VERTICAL));
    }

    public static List<Node> searchRecursively(String toMatch) {
        LinkedList<Node> matched = new LinkedList<>();
        return searchRecursively(toMatch, matched, root);
    }
    public static List<Node> searchRecursively(String toMatch, List<Node> matched, FolderNode root) {
        for (Node n : root.getChildren()) {
            if (n.getName().toLowerCase().contains(toMatch.toLowerCase())) {
                matched.add(n);
            }
            if (n instanceof FolderNode fn) {
                searchRecursively(toMatch, matched, fn);
            }
        }
        return matched;
    }

    public static String renderTree(Node root, List<Node> linearIndex) {
        if (root == null) return "";
        StringBuilder sb = new StringBuilder();

        linearIndex.clear();
        renderRecursive(root, "", true, sb, linearIndex);

        return sb.toString();
    }

    private static void renderRecursive(Node node, String prefix, boolean isTail,
                                        StringBuilder sb, List<Node> linearIndex) {
        linearIndex.add(node);

        if (node.getParent() == null) {
            sb.append(node.getName()).append("\n");
        } else {
            sb.append(prefix)
                    .append(isTail ? "└── " : "├── ")
                    .append(node.getName())
                    .append("\n");
        }

        if (node instanceof FolderNode) {
            FolderNode folder = (FolderNode) node;
            List<Node> children = folder.getChildren();

            for (int i = 0; i < children.size(); i++) {
                boolean isChildTail = (i == children.size() - 1);

                String childPrefix;
                if (node.getParent() == null) {
                    childPrefix = "";
                } else {
                    childPrefix = prefix + (isTail ? "    " : "│   ");
                }

                renderRecursive(children.get(i), childPrefix, isChildTail, sb, linearIndex);
            }
        }
    }

    public static String getAdjustedPath(Node current) {
        if (mainWindow.getSize() == null) {
            return current.getPath(3);
        } else {
            int terminalWidth = mainWindow.getSize().getColumns();
            int max = terminalWidth / 17;
            return current.getPath(max);
        }
    }

}