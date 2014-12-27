4D
==

Firmware and companion desktop software for 4D Systems LCD modules.

[`4dgl/`](https://github.com/kreativekorp/4d/blob/master/4dgl)

The **4dgl** directory contains firmware for 4D Systems LCD modules, written in their proprietary 4DGL language.

[`4dgl/goldelox/Carousel.4dg`](https://github.com/kreativekorp/4d/blob/master/4dgl/goldelox/Carousel.4dg)

**Carousel** displays images, animations, and videos stored in a 4FS file on the MicroSD card. (It is the firmware driving the **Spritelet**.)

[`4dgl/goldelox/4DX.4dg`](https://github.com/kreativekorp/4d/blob/master/4dgl/goldelox/4DX.4dg)

**4DX** rolls virtual dice stored in a 4DX file on the MicroSD card. (It is the firmware driving the **dX:OED Overengineered Electronic Die**.)

[`hardware/`](https://github.com/kreativekorp/4d/tree/master/hardware)

The **hardware** directory contains descriptions of physical items to accompany 4D Systems LCD modules.

[`hardware/joydelox/1.0/`](https://github.com/kreativekorp/4d/tree/master/hardware/joydelox/1.0)

**Joydelox 1.0** is a PCB that connects to a GOLDELOX LCD module to provide joystick input and power.

[`hardware/joydelox/1.1.1/`](https://github.com/kreativekorp/4d/tree/master/hardware/joydelox/1.1.1)

**Joydelox 1.1.1** is a PCB that connects to a GOLDELOX LCD module to provide joystick input, a piezo knock sensor, and power.

[`hardware/spritelet/`](https://github.com/kreativekorp/4d/tree/master/hardware/spritelet)

This directory contains the model for the 3D-printed case of the **Spritelet**.

[`java/4D/src/`](https://github.com/kreativekorp/4d/tree/master/java/4D/src)

The **java** directory contains Java source code for desktop software used with 4D Systems LCD modules.

[`java/4D/src/com/kreative/fourdee/makegci/`](https://github.com/kreativekorp/4d/tree/master/java/4D/src/com/kreative/fourdee/makegci)

**MakeGCI** is a command line utility for converting images to GCI, the native image format of 4D Systems LCD modules.

[`java/4D/src/com/kreative/fourdee/gciview/`](https://github.com/kreativekorp/4d/tree/master/java/4D/src/com/kreative/fourdee/gciview)

**GCIView** is a graphical utility for viewing and converting images. It supports GCI, animated GIF, and any format supported by Java (usually PNG, static GIF, JPEG, BMP, and WBMP).

[`java/4D/src/com/kreative/fourdee/make4fs/`](https://github.com/kreativekorp/4d/tree/master/java/4D/src/com/kreative/fourdee/make4fs)

**Make4FS** is a command line utility for packing files into a 4FS file. 4FS (4D File System) is a simple flat file system used by 4DGL programs such as Carousel. (4FS is an **UNOFFICIAL** format and 4D Systems is **NOT** aware of its existence.)

[`java/4D/src/com/kreative/fourdee/fourfs/`](https://github.com/kreativekorp/4d/tree/master/java/4D/src/com/kreative/fourdee/fourfs)

**FourFS** is a graphical utility for managing files stored inside a 4FS file.

[`java/4D/src/com/kreative/fourdee/dx/`](https://github.com/kreativekorp/4d/tree/master/java/4D/src/com/kreative/fourdee/dx)

**Make4DX** is a command line utility for building the 4DX file used by 4DX from its source files.

[`sources/`](https://github.com/kreativekorp/4d/tree/master/sources)

The **sources** directory contains data files (or source files used to build data files) used by 4DGL programs such as Carousel and 4DX.

[`sources/4DX/`](https://github.com/kreativekorp/4d/tree/master/sources/4DX)

The **4DX** sources directory contains the die face images, die definitions, presets, layouts, and name map for 4DX.

[`sources/Demo Reel/`](https://github.com/kreativekorp/4d/tree/master/sources/Demo%20Reel)

The **Demo Reel** sources directory contains demo images for Carousel in both GIF and GCI format.

[`sources/Names GCI/`](https://github.com/kreativekorp/4d/tree/master/sources/Names%20GCI)

The **Names GCI** sources directory contains marquee animations featuring female given names in GCI format.

[`sources/Names GIF/`](https://github.com/kreativekorp/4d/tree/master/sources/Names%20GIF)

The **Names GIF** sources directory contains marquee animations featuring female given names in GIF format.
