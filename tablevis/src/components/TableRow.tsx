import React from "react";
// import "./App.css";
import TableCell from "./TableCell";
import { useState } from "react";
import axios from "axios";
// @ts-ignore
import { AwesomeButton } from "react-awesome-button";
// @ts-ignore
import "react-awesome-button/dist/styles.css";

interface TableRowInput {
  row: Record<string, string>;
  rowNum: string;
  headerMap: Map<number, string>;
  deleteFunc: (r: Record<string, string>) => void;
}

function TableRow(props: TableRowInput) {
  const loadRow = () => {
    let cells = [];
    cells.push(<TableCell text={props.rowNum} />);
    for (let i = 0; i < props.headerMap.size; i++) {
      let col: string = String(props.headerMap.get(i));
      let value = props.row[col];
      cells.push(<TableCell text={value} />);
    }
    cells.push(
      <td>
        <button
          className="deleteButton"
          onClick={() => {
            console.log("deleting row");
            console.log(props.row);
            props.deleteFunc(props.row);
          }}
        >
          Delete
        </button>
      </td>
    );
    return cells;
  };
  return <tr>{loadRow()}</tr>;
}

export default TableRow;
