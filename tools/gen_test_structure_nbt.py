#!/usr/bin/python3
"""One-shot: writes a minimal 1x1x1 stone structure NBT (gzip). Stdlib only."""
# Must match target Minecraft release for structure NBT (see https://minecraft.wiki/w/Data_version )
DATA_VERSION_MINECRAFT_1_20_1 = 3465

import gzip
import struct
import sys
from pathlib import Path


def _name(name: str) -> bytes:
    b = name.encode("utf-8")
    return struct.pack(">H", len(b)) + b


def tag_int(name: str, value: int) -> bytes:
    return bytes([3]) + _name(name) + struct.pack(">i", value)


def tag_string(name: str, value: str) -> bytes:
    vb = value.encode("utf-8")
    return bytes([8]) + _name(name) + struct.pack(">H", len(vb)) + vb


def tag_list_int(name: str, values: list[int]) -> bytes:
    out = bytes([9]) + _name(name) + bytes([3]) + struct.pack(">i", len(values))
    for v in values:
        out += struct.pack(">i", v)
    return out


def tag_list_compound(name: str, compounds: list[bytes]) -> bytes:
    out = bytes([9]) + _name(name) + bytes([10]) + struct.pack(">i", len(compounds))
    for c in compounds:
        out += c
    return out


def main() -> None:
    out_path = Path(__file__).resolve().parents[1] / (
        "src/testmod/resources/data/mothlib_test/structures/test_structure/base.nbt"
    )
    out_path.parent.mkdir(parents=True, exist_ok=True)

    palette_entry = tag_string("Name", "minecraft:stone") + bytes([0])
    block_entry = tag_int("state", 0) + tag_list_int("pos", [0, 0, 0]) + bytes([0])

    inner = (
        tag_int("DataVersion", DATA_VERSION_MINECRAFT_1_20_1)
        + tag_list_int("size", [1, 1, 1])
        + tag_list_compound("palette", [palette_entry])
        + tag_list_compound("blocks", [block_entry])
    )

    nbt = bytes([10, 0, 0]) + inner + bytes([0])

    with gzip.open(out_path, "wb", compresslevel=9) as f:
        f.write(nbt)

    print(out_path, out_path.stat().st_size, "bytes", file=sys.stderr)


if __name__ == "__main__":
    main()
