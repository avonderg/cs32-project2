import React, {useState} from 'react';
import Completed from '../Completed/Completed'
import InProgress from '../InProgress/InProgress'
import NeedToStart from '../NeedToStart/NeedToStart'
import Unassigned from '../Unassigned/Unassigned'

import "../../styles.css"

type table = {

    headers: string[]
    rows: Record<string,string>[];
    // cols: Record<string,string>[];
}

type kanban_info = {
    kanban_status: string[]
    kanban_text: string[][]
}

type tableprop = {
    tableInfo: table;
    setRowMap: (value: table) => void;
}

type TableValues = {
    need_to_start: string[][],
    unassigned: string[][],
    in_progress: string[][],
    completed: string[][]
}


function Table(props: tableprop) {

    const rowMap = props.tableInfo.rows
    // const colMap = props.tableInfo.cols

    const kb_status:string[] = [];
    const kb_text:string[][] = [];


    var output_table: kanban_info = {
        kanban_status: kb_status,
        kanban_text: kb_text
    };

    for (let i = 0; i < rowMap?.length; i++) {

        const relevant_columns:string[] = []
        relevant_columns[0] = "block_id"
        relevant_columns[1] = "block_title"
        relevant_columns[2] = "block_content"
        relevant_columns[3] = "block_date"
        const curr_row = rowMap[i]
        const curr_priority = rowMap[i]["column"]
        // console.log(rowMap)
        output_table.kanban_status[i] = curr_priority
        kb_text.push([])

        // Save relevant columns from the database into the kanban datatype.
        for(let j = 0; j < props.tableInfo.headers.length; j++){
            var header_name: string = props.tableInfo.headers[j]
            if (relevant_columns.includes(header_name)){
                kb_text[i].push(curr_row[props.tableInfo.headers[j]])
            }

        }
    }

    const not_started:string[][] = []
    const unassigned_text:string[][] = []
    const in_progress_text:string[][] = []
    const completed_text:string[][] = []
    const table_helper: TableValues = {
        need_to_start: not_started,
        unassigned: unassigned_text,
        in_progress: in_progress_text,
        completed: completed_text

    }

    console.log(output_table)
    for (var i = 0; i < output_table.kanban_status.length; i++) {
        var table_content:string = ""
        for(let j = 0; j < output_table.kanban_text[i].length; j++) {
            table_content = table_content + "\n" + output_table.kanban_text[i][j]
        }
        if (output_table.kanban_status[i] == "1"){
            console.log("unassigned")
            unassigned_text.push(output_table.kanban_text[i])
        }
        else if(output_table.kanban_status[i] == "2"){
            console.log("need to start")
            not_started.push(output_table.kanban_text[i])
        }
        else if(output_table.kanban_status[i] == "3"){
            console.log("in progress")
            in_progress_text.push(output_table.kanban_text[i])
        }
        else if (output_table.kanban_status[i] == "4"){
            completed_text.push(output_table.kanban_text[i])
        }
    }

    console.log(output_table)

    return(
        <table className = "kanbanTable" id = "kanban_display">
            <tbody>
            <tr>
                <th>Need to Start</th>
                <th>Unassigned</th>
                <th>In Progress</th>
                <th>Completed</th>
            </tr>
            {table_helper.need_to_start.map(item => <Unassigned text={item}/>)}
            {table_helper.unassigned.map(item => <NeedToStart text={item}/>)}
            {table_helper.completed.map(item => <Completed text={item}/>)}
            {table_helper.in_progress.map(item => <InProgress text={item}/>)}
            </tbody>
        </table>
    )

}



export default Table;