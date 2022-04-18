import React from "react";
// import "./App.css";
import { useState } from "react";
import axios from "axios";
// @ts-ignore
import { AwesomeButton } from "react-awesome-button";
// @ts-ignore
import "react-awesome-button/dist/styles.css";

interface TableCellInput {
  text: string;
}

function TableCell(props: TableCellInput) {
  return <td>{props.text}</td>;
}

export default TableCell;
