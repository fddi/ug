"use client"

import { Fragment } from "react"
import MainList from "../components/MainList"


export default function AccountInfo({ children }) {
    return (
        <Fragment>
            <MainList defaultData={[{ loading: true }]} />
        </Fragment>
    )
}