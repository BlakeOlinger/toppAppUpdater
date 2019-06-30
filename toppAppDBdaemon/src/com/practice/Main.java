package com.practice;

// this daemon is designed to interface between git repository db and
// the app install directory to automate git functions and
// later automate app updates

public class Main {

    public static void main(String[] args) {
        // the cmd.exe process is headless - cmd.exe /c notepad.exe opens
        // an instance of notepad - the process.waitFor() doesn't return
        // until the notepad window closes
        // may be helpful to output the cmd.exe via cmd.exe to a log file
        // for review
        // "cmd.exe /c git status > test.log" - successfully output to file
        // git commands this way work as expected

        // process = Runtime.getRuntime().exec("cmd.exe /c git status > test.log");

        new Config().monitorProgramState();

        new PullDaemon().start();

    }
}
