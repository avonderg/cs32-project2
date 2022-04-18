import React from "react";

// interface representing the information for a TableCell
interface TableCellInput {
  text: string;
}

/**
 * creates a table cell.
 * @param props a TableCellInput, the information that goes into the Tablecell
 * @returns 
 */
function TableCell(props: TableCellInput) {
  return <td>{props.text}</td>;
}

export default TableCell;
