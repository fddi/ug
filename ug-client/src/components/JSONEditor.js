import JSONEditor from 'jsoneditor/dist/jsoneditor-minimalist';
import 'jsoneditor/dist/jsoneditor.css';
import { useEffect, useRef, useState } from 'react';
let editor;
export default function CompJSONEditor({ style, json, onChange, ...rest }) {
    const divRef = useRef(null)

    function handleChange() {
        if (editor && onChange) {
            const currentJson = editor.get();
            if (json !== currentJson) {
                onChange(currentJson);
            }
        }
    }

    function createEditor() {
        editor = new JSONEditor(divRef.current, {
            mode:'tree',
            onChange: handleChange,
            ...rest
        })
    }

    useEffect(() => {
        createEditor()
        return (() => editor && editor.destroy())
    }, [])

    useEffect(() => {
        if (json == null || editor == null) return;
        const currentJson = editor.getText();
        if (JSON.stringify(json) != currentJson) {
            editor.set(json)
        }
    }, [json])

    return (
        <div ref={divRef} style={style || {}}></div>
    )
}