package com.kakaopay.codingtest

import org.springframework.stereotype.Service
import java.io.File
import java.nio.charset.Charset
import kotlin.math.roundToInt

@Service
class FinanceService(
        val instituteRepository: InstituteRepository,
        val financeDataRepository: FinanceDataRepository
) {

    fun loadData(charsetName: String): Boolean {
        clearAllData()
        val file = File("./doc/data.csv")
        val instituteCodes = arrayListOf<String>()
        file.forEachLine(Charset.forName(charsetName)) {
            val dataList = splitCsvLine(it)
                    .filter{  data -> data.isNotEmpty() }
            if(dataList[0].toIntOrNull() == null) {
                instituteCodes.addAll(updateInstitutes(dataList.subList(2, dataList.size)))
            } else {
                updateFinanceData(instituteCodes, dataList[0].toInt(), dataList[1].toInt(), dataList.subList(2, dataList.size))
            }
        }
        return true
    }

    private fun splitCsvLine(line: String):List<String> {
        val list = arrayListOf<String>()
        var isInside = false
        var word = ""
        line.forEach {
            if (it == '\"') isInside = !isInside
            else if (it == ',' ) {
                if( !isInside) {
                    list.add(word)
                    word = ""
                }
            }
            else word += it
        }
        list.add(word)
        return list
    }

    private fun updateFinanceData(instituteCodes: List<String>, year: Int, month: Int, dataList: List<String>) {
        val financeDataList = dataList.mapIndexed { index, amount ->
            FinanceData(year, month, instituteCodes[index], amount.toInt())
        }
        financeDataRepository.saveAll(financeDataList)
    }

    private fun updateInstitutes(dataList: List<String>): List<String> {
        val institutes = dataList.map{
            var name = it.dropLast("(억원)".length)
            if (name.contains("1)")) name = name.dropLast("1)".length)
            Institute(findInstituteCodeForName(name), name)
        }
        instituteRepository.saveAll(institutes)
        return institutes.map{ it.code }
    }


    private fun clearAllData() {
        instituteRepository.deleteAll()
        financeDataRepository.deleteAll()
    }

    fun getInstitutes(): List<String> {
        return instituteRepository.findAll().map{ it.name }
    }

    fun getAllFinanceInfoByYear(): AllFinanceInfoByYear {
        val all = financeDataRepository.findAll()

        val infoListByYear = arrayListOf<FinanceInfoByYear>()
        all.groupBy { it.year }.forEach { (year, dataList) ->
            val detailAmount = getDetailAmount(dataList)
            infoListByYear.add(FinanceInfoByYear(year, detailAmount.values.sum(), detailAmount))
        }

        return AllFinanceInfoByYear("주택금융 공급현황", infoListByYear)
    }

    private fun getDetailAmount(dataList: List<FinanceData>): Map<String, Int> {
        val detailAmount = hashMapOf<String, Int>()
        dataList.groupBy { it.code }.forEach { (code, list) ->
            detailAmount[instituteCodeToName(code)] = list.sumBy { it.amount }
        }
        return detailAmount
    }

    fun getMostFinanceForAllYears(): MostFinanceForAllYears {
        val all = financeDataRepository.findAll()

        var maxYear = 0
        var maxBankCode = ""
        var maxAmount = 0
        all.groupBy { it.year }.mapValues { entry ->
            val mf = findMostFinanceForAYear(entry.value)
            if (mf.amount > maxAmount) {
                maxYear = entry.key
                maxBankCode = mf.code
                maxAmount = mf.amount
            }
        }

        return MostFinanceForAllYears(maxYear, instituteCodeToName(maxBankCode))
    }

    private fun findMostFinanceForAYear(dataList: List<FinanceData>): MostFinanceForAYear {
        var maxBankCode = ""
        var maxAmount = 0

        dataList.groupBy { it.code }.forEach { (code, list) ->
            val sum = list.sumBy { it.amount }
            if (sum > maxAmount) {
                maxAmount = sum
                maxBankCode = code
            }
        }

        return MostFinanceForAYear(maxBankCode, maxAmount)
    }

    fun getMostAndLeastOf(instituteCode: String): MostAndLeast {
        val fdMap = financeDataRepository.findAllByCode(instituteCode)
                .groupBy { it.year }
                .filter{entry ->
                    entry.key != 2017
                }


        var minYear = 0
        var minAmount = Int.MAX_VALUE
        var maxYear = 0
        var maxAmount = 0


        fdMap.forEach { (year, list) ->
            val avg = (list.sumBy { it.amount } / 12.00).roundToInt()
            if (avg < minAmount) {
                minAmount = avg
                minYear = year
            }
            if (avg > maxAmount) {
                maxAmount = avg
                maxYear = year
            }
        }

        return MostAndLeast(
                instituteCodeToName(instituteCode),
                arrayListOf(AmountOfYear(minYear, minAmount), AmountOfYear(maxYear, maxAmount)))
    }
}