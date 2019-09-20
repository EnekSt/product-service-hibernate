package com.example.psh.utils.convertors;

import com.example.psh.entities.Parameter;
import org.example.soap.ParameterInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParameterConverter {

    // Parameter to ParameterInfo

    public ParameterInfo convertToParameterInfo(Parameter parameter) {
        ParameterInfo parameterInfo = new ParameterInfo();
        BeanUtils.copyProperties(parameter, parameterInfo);
        return parameterInfo;
    }

    public List<ParameterInfo> convertToParameterInfoList(List<Parameter> parameters) {
        List<ParameterInfo> parameterInfos = new ArrayList<>();
        for (Parameter parameter : parameters) {
            parameterInfos.add(convertToParameterInfo(parameter));
        }
        return parameterInfos;
    }

    // ParameterInfo to Parameter

    public Parameter convertToParameter(ParameterInfo parameterInfo) {
        Parameter parameter = new Parameter();
        //BeanUtils.copyProperties(parameterInfo, parameter);
        parameter.setKey(parameterInfo.getKey());
        parameter.setValue(parameterInfo.getValue());
        return parameter;
    }

    public List<Parameter> convertToParameterList(List<ParameterInfo> parameterInfos) {
        List<Parameter> parameters = new ArrayList<>();
        for (ParameterInfo parameterInfo : parameterInfos) {
            parameters.add(convertToParameter(parameterInfo));
        }
        return parameters;
    }

}
