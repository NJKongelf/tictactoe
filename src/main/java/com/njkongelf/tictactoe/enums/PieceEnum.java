package com.njkongelf.tictactoe.enums;

public enum PieceEnum {
    X("X"),
    O("O");
    private String message;

    PieceEnum(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
