package com.github.mh321Productions.handball4AllApi.api.data

interface RawData<TData> {
    fun getData(): TData
}