# Create training data depending on the number of players and make it model ready
# Create a file for each tile -> test

from glob import glob
import pandas as pd
import re
from rich import print
from tqdm import tqdm
from sklearn.model_selection import train_test_split

# Target Columns:
# tile, Current tiles in rack, (Current Board, Board-1)*times n, target (opponent, rest)


def one_hot_encoding_rack(input) -> list[int]:
    output = [0] * 53  # 53 tiles are in the board
    for tile in input:
        output[tile - 1] = 1  # -1 as the index starts at 0
    return output


def one_hot_encoding_board(board) -> list[int]:
    output = [0] * 679  # number of sets
    for set_in_board in board:
        output[ALL_SETS.index(set_in_board)] = 1
    return output


def create_training_data(src: str, dst: str) -> None:
    df = pd.read_csv(
        src, sep=r",\s+", encoding="utf-8", skipinitialspace=True, engine="python"
    )
    df = df.rename(columns={"HandOne": "RackOne", "HandTwo": "RackTwo"})

    # df["Board"] = df["Board"].str.split(";")

    df["Board"] = df["Board"].apply(
        lambda x: [
            [int(j) for j in str(i).strip().split(" ")] for i in str(x).split(";")
        ]
        if str(x) != "nan"
        else ""
    )

    df["Board"] = df["Board"].apply(one_hot_encoding_board)

    df["RackOne"] = df["RackOne"].apply(
        lambda x: [int(i) for i in x.strip().split(" ")] if str(x) != "nan" else []
    )
    df["RackOne"] = df["RackOne"].apply(one_hot_encoding_rack)
    df["RackTwo"] = df["RackTwo"].apply(
        lambda x: [int(i) for i in x.strip().split(" ")] if str(x) != "nan" else []
    )
    df["RackTwo"] = df["RackTwo"].apply(one_hot_encoding_rack)

    # get the first n lines for the first n boards (ordered by move number asc)
    # one line consists of player one rack + (board before current move (same line)
    # + board of line before (difference represent the opportunities)) *times n
    tile_df = []
    for i in range(len(df)):  # go through every line in our data
        # create input
        line = {
            "target": df["RackTwo"].iloc[i],
            "rack": df["RackOne"].iloc[i],
            "board": df["Board"].iloc[i],
        }
        past_boards = 6
        for j in range(past_boards + 1):
            if i - j < 0:
                line[f"board-{j}"] = [0] * 679
            else:
                line[f"board-{j}"] = df["Board"].iloc[i - j]
        if sum(line["board"]) == 0 or sum(line["board-0"]) == 0:
            # Dont use these values as a training sample if we only have one move
            continue

        tile_df.append(line)
    tile_df = pd.DataFrame(tile_df)  # convert list of dicts to df
    tile_df.to_csv(
        dst + "training_data_game_" + re.findall(r"\d+", src)[-1] + ".csv", index=False
    )
    pass


if __name__ == "__main__":
    with open(r"data\allSets.txt", "r") as f:
        ALL_SETS = [
            [int(x) for x in re.sub(r"\[|\]|\n", "", i).split(", ")]
            for i in f.readlines()
        ]
    src_root = r"data\raw_data\baseline_vs_baseline\\"
    dst = r"data\training_data\baseline_vs_baseline\\"
    # print(encode_Tiles_2_number("black|13"))
    for src in tqdm(list(glob(src_root + "**"))):
        create_training_data(src=src, dst=dst)

    dfs = [pd.read_csv(path) for path in glob(dst + "**")]
    combined_df = pd.concat(dfs)
    train, test = train_test_split(combined_df, test_size=0.3, random_state=42)
    print("Length of train:", len(train))
    print("Length of test :", len(test))
    train.to_csv(r"data\training_data\train_baseline_vs_baseline_data.csv", index=False)
    test.to_csv(r"data\training_data\test_baseline_vs_baseline_data.csv", index=False)
