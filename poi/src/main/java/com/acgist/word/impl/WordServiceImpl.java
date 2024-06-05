package com.acgist.report.word.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.acgist.data.report.entity.ReportHistory;
import com.acgist.data.report.entity.ReportModelInstance;
import com.acgist.data.report.vo.ReportAlarmCountVo;
import com.acgist.data.report.vo.ReportAlarmLevelVo;
import com.acgist.data.report.vo.ReportModelInstanceResultVo;
import com.acgist.report.counter.ReportResult;
import com.acgist.report.service.ReportModelInstanceService;
import com.acgist.report.word.IWordService;
import com.acgist.report.word.module.BarWordModule;
import com.acgist.report.word.module.ErrorWordModule;
import com.acgist.report.word.module.LineWordModule;
import com.acgist.report.word.module.PieWordModule;
import com.acgist.report.word.module.TableWordModule;
import com.acgist.report.word.module.TextWordModule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordServiceImpl implements IWordService {

    private final ReportModelInstanceService reportModelInstanceService;

    /**
     * 模型类型
     */
    public enum Type {
        BAR,
        // 折线图
        // 饼图
        PIE,
        // 柱状图
        LINE,
        // 文本
        TEXT,
        // 告警趋势
        ALARM_COUNT,
        // 告警等级
        ALARM_LEVEL,
    }
    
    // 路径 = 类型
    private static final Map<String, Type> MAPPING = new HashMap<>();
    
    static {
        // TODO: 前端定义以后修改
        MAPPING.put("/pie", Type.PIE);
        MAPPING.put("/bar", Type.BAR);
        MAPPING.put("/line", Type.LINE);
        MAPPING.put("/alarm_count", Type.ALARM_COUNT);
        MAPPING.put("/alarm_level", Type.ALARM_LEVEL);
    }
    
    @Override
    public boolean build(File file, ReportHistory reportHistory) {
        Objects.requireNonNull(file, "文件为空");
        Objects.requireNonNull(reportHistory, "报表记录为空");
        // 删除旧的记录
        if(file.exists()) {
            file.delete();
        }
        try(
            final XWPFDocument document = new XWPFDocument();
            final OutputStream output   = new FileOutputStream(file);
        ) {
            this.setStyle(document);
            if(this.reportModelInstanceService == null) {
                final ReportModelInstance instance = new ReportModelInstance();
                instance.setModelName("柱状图模块");
                this.buildBar(instance, document, reportHistory);
                instance.setModelName("饼图模块");
                this.buildPie(instance, document, reportHistory);
                instance.setModelName("折线图模块");
                this.buildLine(instance, document, reportHistory);
                instance.setModelName("文本模块");
                this.buildText(instance, document, reportHistory);
                instance.setModelName("异常模块");
                this.buildError(instance, document, "测试异常");
                instance.setModelName("表格模块");
                this.buildAlarmLevel(instance, document, reportHistory);
            } else {
                // 线上代码
                final List<ReportModelInstance> list = this.reportModelInstanceService.list(Wrappers.lambdaQuery(ReportModelInstance.class).eq(ReportModelInstance::getReportId, reportHistory.getReportConfigId()));
                list.sort((a, z) -> a.getSeq() == null || z.getSeq() == null ? 0 : a.getSeq().compareTo(z.getSeq()));
                list.forEach(instance -> {
                    try {
                        final Type type = MAPPING.get(instance.getModelPath());
                        switch (type) {
                        case BAR:
                            this.buildBar(instance, document, reportHistory);
                            break;
                        case PIE:
                            this.buildPie(instance, document, reportHistory);
                            break;
                        case LINE:
                            this.buildLine(instance, document, reportHistory);
                            break;
                        case TEXT:
                            this.buildText(instance, document, reportHistory);
                            break;
                        case ALARM_COUNT:
                            this.buildAlarmCount(instance, document, reportHistory);
                            break;
                        case ALARM_LEVEL:
                            this.buildAlarmLevel(instance, document, reportHistory);
                            break;
                        default:
                            log.warn("没有适配模板类型：{} - {} - {}", reportHistory.getId(), instance.getId(), instance.getModelPath());
                            break;
                        }
                    } catch (Exception e) {
                        log.error("文档模块创建异常：{} - {}", reportHistory.getId(), instance.getId(), e);
                        this.buildError(instance, document, e.getMessage());
                    }
                });
            }
            document.write(output);
        } catch (Exception e) {
            log.error("文件导出异常", e);
        }
        return true;
    }
    
    /**
     * 基本样式
     * 
     * @param document 文档
     */
    private void setStyle(XWPFDocument document) {
        document.createHeader(HeaderFooterType.FIRST);
        document.createFooter(HeaderFooterType.FIRST);
//      document.addEndnote(null);
//      document.addFootnote(null);
    }

    /**
     * 文档错误信息
     * 
     * @param instance 实例
     * @param document 文档
     * @param message  错误信息
     */
    private void buildError(ReportModelInstance instance, XWPFDocument document, String message) {
        final ErrorWordModule errorWordModule = new ErrorWordModule(message, instance, document);
        errorWordModule.buildModule();
    }
    
    /**
     * 柱状图内容
     * 
     * @param instance 实例
     * @param document 文档
     * @param reportHistory 历史报表
     */
    private void buildBar(ReportModelInstance instance, XWPFDocument document, ReportHistory reportHistory) {
        final BarWordModule barWordModule = new BarWordModule(instance, document);
        if(this.reportModelInstanceService == null) {
            final Map<String, Integer> map = new LinkedHashMap<>();
            map.put("销量1", 10);
            map.put("销量2", 30);
            map.put("销量3", 40);
            map.put("销量4", 10);
            barWordModule.setData(map);
        } else {
            final ReportModelInstanceResultVo<Map<String, List<ReportResult>>> resultVo = this.reportModelInstanceService.selectReportItemsVo(instance, reportHistory.getReportDate());
            barWordModule.setData(
                resultVo.getData().entrySet().stream().map(v -> {
                    return Map.entry(v.getKey(), v.getValue().isEmpty() ? 0 : v.getValue().get(0).getCount().intValue());
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, z) -> z, LinkedHashMap::new))
            );
        }
        barWordModule.buildModule();
    }
    
    /**
     * 饼图内容
     * 
     * @param instance 实例
     * @param document 文档
     * @param reportHistory 历史报表
     */
    private void buildPie(ReportModelInstance instance, XWPFDocument document, ReportHistory reportHistory) {
        final PieWordModule pieWordModule = new PieWordModule(instance, document);
        if(this.reportModelInstanceService == null) {
            final Map<String, Integer> map = new LinkedHashMap<>();
            map.put("销量1", 10);
            map.put("销量2", 30);
            map.put("销量3", 40);
            map.put("销量4", 10);
            pieWordModule.setData(map);
        } else {
            final ReportModelInstanceResultVo<Map<String, List<ReportResult>>> resultVo = this.reportModelInstanceService.selectReportItemsVo(instance, reportHistory.getReportDate());
            pieWordModule.setData(
                resultVo.getData().entrySet().stream().map(v -> {
                    return Map.entry(v.getKey(), v.getValue().isEmpty() ? 0 : v.getValue().get(0).getCount().intValue());
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, z) -> z, LinkedHashMap::new))
            );
        }
        pieWordModule.buildModule();
    }
    
    /**
     * 折线图内容
     * 
     * @param instance 实例
     * @param document 文档
     * @param reportHistory 历史报表
     */
    private void buildLine(ReportModelInstance instance, XWPFDocument document, ReportHistory reportHistory) {
        final LineWordModule lineWordModule = new LineWordModule(instance, document);
        if(this.reportModelInstanceService == null) {
            final Map<String, List<Integer>> map = new LinkedHashMap<>();
            map.put("销量1", List.of(10, 20, 30, 40));
            map.put("销量2", List.of(20, 20, 30, 100));
            map.put("销量3", List.of(30, 20, 50, 90));
            map.put("销量4", List.of(40, 20, 60, 40));
            lineWordModule.setData(map);
        } else {
            final ReportModelInstanceResultVo<Map<String, List<ReportResult>>> resultVo = this.reportModelInstanceService.selectReportItemsVo(instance, reportHistory.getReportDate());
            lineWordModule.setData(
                resultVo.getData().entrySet().stream().map(v -> {
                    return Map.entry(v.getKey(), v.getValue().isEmpty() ? List.<Integer>of() : v.getValue().stream().map(x -> x.getCount().intValue()).collect(Collectors.toList()));
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, z) -> z, LinkedHashMap::new))
            );
        }
        lineWordModule.buildModule();
    }
    
    /**
     * 文本内容
     * 
     * @param instance 实例
     * @param document 文档
     * @param reportHistory 历史报表
     */
    private void buildText(ReportModelInstance instance, XWPFDocument document, ReportHistory reportHistory) {
        final TextWordModule textWordModule = new TextWordModule(true, instance, document);
        if(this.reportModelInstanceService == null) {
            textWordModule.setText("<h1>文本标题</h1><div style=\"font-weight:bold;font-size:12px;color:#C00;\"><p>第一行内容</p></div><p>第一行内容</p>");
        } else {
            final ReportModelInstanceResultVo<String> resultVo = this.reportModelInstanceService.selectReportTextVo(instance, reportHistory.getReportDate());
            textWordModule.setText(resultVo.getData());
        }
        textWordModule.buildModule();
    }

    /**
     * 告警级别
     * 
     * @param instance 实例
     * @param document 文档
     * @param reportHistory 历史报表
     */
    private void buildAlarmLevel(ReportModelInstance instance, XWPFDocument document, ReportHistory reportHistory) {
        final TableWordModule tableWordModule = new TableWordModule(instance, document);
        tableWordModule.setHeader(List.of("对象名称", "设备名称", "告警总数", "紧急告警总数", "重要告警总数", "次要告警总数", "一般告警总数"));
        if(this.reportModelInstanceService == null) {
            tableWordModule.setData(List.of(
                List.of("对象名称1", "设备名称1", 1, 2, 3, 4, 5),
                List.of("对象名称2", "设备名称2", 1, 2, 3, 4, 5),
                List.of("对象名称3", "设备名称3", 1, 2, 3, 4, 5),
                List.of("对象名称4", "设备名称4", 1, 2, 3, 4, 5)
            ));
        } else {
            final ReportModelInstanceResultVo<List<ReportAlarmLevelVo>> resultVo = this.reportModelInstanceService.selectReportAlarmLevelVo(instance, reportHistory.getReportDate());
            final List<List<Object>> data = resultVo.getData().stream()
                .map(v -> {
                    final List<Object> list = new ArrayList<>();
                    list.add(v.getObjectName());
                    list.add(v.getDeviceName());
                    list.add(v.getAlarmCount());
                    list.add(v.getUrgentCount());
                    list.add(v.getMajorCount());
                    list.add(v.getMinorCount());
                    list.add(v.getGeneralCount());
                    return list;
                })
                .collect(Collectors.toList());
            tableWordModule.setData(data);
        }
        tableWordModule.buildModule();
    }
    
    /**
     * 告警趋势
     * 
     * @param instance 实例
     * @param document 文档
     * @param reportHistory 历史报表
     */
    private void buildAlarmCount(ReportModelInstance instance, XWPFDocument document, ReportHistory reportHistory) {
        final LineWordModule lineWordModule = new LineWordModule(instance, document);
        if(this.reportModelInstanceService == null) {
            final Map<String, List<Integer>> map = new LinkedHashMap<>();
            map.put("销量1", List.of(10, 20, 30, 40));
            map.put("销量2", List.of(20, 20, 30, 100));
            map.put("销量3", List.of(30, 20, 50, 90));
            map.put("销量4", List.of(40, 20, 60, 40));
            lineWordModule.setData(map);
        } else {
            final ReportModelInstanceResultVo<Map<String, List<ReportAlarmCountVo>>> resultVo = this.reportModelInstanceService.selectReportAlarmCountVo(instance, reportHistory.getReportDate());
            lineWordModule.setData(
                resultVo.getData().entrySet().stream().map(v -> {
                    return Map.entry(v.getKey(), v.getValue().isEmpty() ? List.<Integer>of() : v.getValue().stream().map(x -> x.getCount().intValue()).collect(Collectors.toList()));
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, z) -> z, LinkedHashMap::new))
            );
        }
        lineWordModule.buildModule();
    }
    
}
