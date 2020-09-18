<template>
    <div>
        <div>
            <div style="display: flex;justify-content: space-between">
                <div>
                    <el-input placeholder="type employee name to search" prefix-icon="el-icon-search"
                              clearable
                              @clear="initEmps"
                              style="width: 350px;margin-right: 10px" v-model="keyword"
                              @keydown.enter.native="initEmps" :disabled="showAdvanceSearchView"></el-input>
                    <el-button icon="el-icon-search" type="primary" @click="initEmps" :disabled="showAdvanceSearchView">
                        search
                    </el-button>
                    <el-button type="primary" @click="showAdvanceSearchView = !showAdvanceSearchView">
                        <i :class="showAdvanceSearchView?'fa fa-angle-double-up':'fa fa-angle-double-down'"
                           aria-hidden="true"></i>
                        advanced search
                    </el-button>
                </div>
                <div>
                    <el-upload
                            :show-file-list="false"
                            :before-upload="beforeUpload"
                            :on-success="onSuccess"
                            :on-error="onError"
                            :disabled="importDataDisabled"
                            style="display: inline-flex;margin-right: 8px"
                            action="/employee/basic/import">
                        <el-button :disabled="importDataDisabled" type="success" :icon="importDataBtnIcon">
                            {{importDataBtnText}}
                        </el-button>
                    </el-upload>
                    <el-button type="success" @click="exportData" icon="el-icon-download">
                        export data
                    </el-button>
                    <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
                        add user
                    </el-button>
                </div>
            </div>
            <transition name="slide-fade">
                <div v-show="showAdvanceSearchView"
                     style="border: 1px solid #409eff;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0px;">
                    <el-row>
                        <el-col :span="5">
                            politic status:
                            <el-select v-model="searchValue.politicId" placeholder="politic status" size="mini"
                                       style="width: 130px;">
                                <el-option
                                        v-for="item in politicsstatus"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4">
                            race:
                            <el-select v-model="searchValue.nationId" placeholder="race" size="mini"
                                       style="width: 130px;">
                                <el-option
                                        v-for="item in nations"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4">
                            position:
                            <el-select v-model="searchValue.posId" placeholder="position" size="mini" style="width: 130px;">
                                <el-option
                                        v-for="item in positions"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4">
                            job level:
                            <el-select v-model="searchValue.jobLevelId" placeholder="job level" size="mini"
                                       style="width: 130px;">
                                <el-option
                                        v-for="item in joblevels"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="7">
                            contract:
                            <el-radio-group v-model="searchValue.engageForm">
                                <el-radio label="employee contract">employee contract</el-radio>
                                <el-radio label="labor contract">labor contract</el-radio>
                            </el-radio-group>
                        </el-col>
                    </el-row>
                    <el-row style="margin-top: 10px">
                        <el-col :span="5">
                            department:
                            <el-popover
                                    placement="right"
                                    title="choose department"
                                    width="200"
                                    trigger="manual"
                                    v-model="popVisible2">
                                <el-tree default-expand-all :data="allDeps" :props="defaultProps"
                                         @node-click="searchViewHandleNodeClick"></el-tree>
                                <div slot="reference"
                                     style="width: 130px;display: inline-flex;font-size: 13px;border: 1px solid #dedede;height: 26px;border-radius: 5px;cursor: pointer;align-items: center;padding-left: 8px;box-sizing: border-box;margin-left: 3px"
                                     @click="showDepView2">{{inputDepName}}
                                </div>
                            </el-popover>
                        </el-col>
                        <el-col :span="10">
                            begin contract:
                            <el-date-picker
                                    v-model="searchValue.beginDateScope"
                                    type="daterange"
                                    size="mini"
                                    unlink-panels
                                    value-format="yyyy-MM-dd"
                                    range-separator="至"
                                    start-placeholder="begin date"
                                    end-placeholder="end date">
                            </el-date-picker>
                        </el-col>
                        <el-col :span="5" :offset="4">
                            <el-button size="mini">cancel</el-button>
                            <el-button size="mini" icon="el-icon-search" type="primary" @click="initEmps('advanced')">search</el-button>
                        </el-col>
                    </el-row>
                </div>
            </transition>
        </div>
        <div style="margin-top: 10px">
            <el-table :data="emps" stripe border v-loading="loading" element-loading-text="loading..."
                    element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)"
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="name"
                        fixed
                        align="left"
                        label="name"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="workID"
                        label="workID"
                        align="left"
                        width="85">
                </el-table-column>
                <el-table-column
                        prop="gender"
                        label="gender"
                        align="left"
                        width="85">
                </el-table-column>
                <el-table-column
                        prop="birthday"
                        width="95"
                        align="left"
                        label="birthday">
                </el-table-column>
                <el-table-column
                        prop="idCard"
                        width="150"
                        align="left"
                        label="idCard">
                </el-table-column>
                <el-table-column
                        prop="wedlock"
                        width="70"
                        label="wedlock">
                </el-table-column>
                <el-table-column
                        prop="nation.name"
                        width="50"
                        label="nation">
                </el-table-column>
                <el-table-column
                        prop="nativePlace"
                        width="80"
                        label="nativePlace">
                </el-table-column>
                <el-table-column
                        prop="politicsstatus.name"
                        label="politic status">
                </el-table-column>
                <el-table-column
                        prop="email"
                        width="180"
                        align="left"
                        label="email">
                </el-table-column>
                <el-table-column
                        prop="phone"
                        width="100"
                        align="left"
                        label="phone">
                </el-table-column>
                <el-table-column
                        prop="address"
                        width="220"
                        align="left"
                        label="address">
                </el-table-column>
                <el-table-column
                        prop="department.name"
                        width="100"
                        align="left"
                        label="department">
                </el-table-column>
                <el-table-column
                        prop="position.name"
                        width="100"
                        label="position">
                </el-table-column>
                <el-table-column
                        prop="jobLevel.name"
                        width="100"
                        label="jobLevel">
                </el-table-column>
                <el-table-column
                        prop="engageForm"
                        width="100"
                        align="left"
                        label="engageForm">
                </el-table-column>
                <el-table-column
                        prop="tiptopDegree"
                        width="80"
                        align="left"
                        label="tiptopDegree">
                </el-table-column>
                <el-table-column
                        prop="specialty"
                        width="150"
                        align="left"
                        label="specialty">
                </el-table-column>
                <el-table-column
                        prop="school"
                        width="150"
                        align="left"
                        label="school">
                </el-table-column>
                <el-table-column
                        prop="beginDate"
                        width="95"
                        align="left"
                        label="beginDate">
                </el-table-column>
                <el-table-column
                        prop="conversionTime"
                        width="95"
                        align="left"
                        label="conversionTime">
                </el-table-column>
                <el-table-column
                        prop="beginContract"
                        width="95"
                        align="left"
                        label="beginContract">
                </el-table-column>
                <el-table-column
                        prop="endContract"
                        width="95"
                        align="left"
                        label="endContract">
                </el-table-column>
                <el-table-column
                        width="100"
                        align="left"
                        label="contract remaining time">
                    <template slot-scope="scope">
                        <el-tag>{{scope.row.contractTerm}}</el-tag>
                        year
                    </template>
                </el-table-column>
                <el-table-column
                        fixed="right"
                        width="200"
                        label="operation">
                    <template slot-scope="scope">
                        <el-button @click="showEditEmpView(scope.row)" style="padding: 3px" size="mini">edit</el-button>
                        <el-button style="padding: 3px" size="mini" type="primary">advanced data</el-button>
                        <el-button @click="deleteEmp(scope.row)" style="padding: 3px" size="mini" type="danger">delete
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div style="display: flex;justify-content: flex-end">
                <el-pagination
                        background
                        @current-change="currentChange"
                        @size-change="sizeChange"
                        layout="sizes, prev, pager, next, jumper, ->, total, slot"
                        :total="total">
                </el-pagination>
            </div>
        </div>
        <el-dialog
                :title="title"
                :visible.sync="dialogVisible"
                width="80%">
            <div>
                <el-form :model="emp" :rules="rules" ref="empForm">
                    <el-row>
                        <el-col :span="5">
                            <el-form-item label="name:" prop="name">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit" v-model="emp.name"
                                          placeholder="employee name"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="gender:" prop="gender">
                                <el-radio-group v-model="emp.gender">
                                    <el-radio label="male">male</el-radio>
                                    <el-radio label="female">female</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="birthday:" prop="birthday">
                                <el-date-picker
                                        v-model="emp.birthday"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 150px;"
                                        placeholder="birthday">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="politic status:" prop="politicId">
                                <el-select v-model="emp.politicId" placeholder="politic status" size="mini" style="width: 200px;">
                                    <el-option
                                            v-for="item in politicsstatus"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="nation:" prop="nationId">
                                <el-select v-model="emp.nationId" placeholder="nation" size="mini" style="width: 150px;">
                                    <el-option
                                            v-for="item in nations"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="nativePlace:" prop="nativePlace">
                                <el-input size="mini" style="width: 120px" prefix-icon="el-icon-edit"
                                          v-model="emp.nativePlace" placeholder="nativePlace"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="email:" prop="email">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-message"
                                          v-model="emp.email" placeholder="email"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="address:" prop="address">
                                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                                          v-model="emp.address" placeholder="address"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="position:" prop="posId">
                                <el-select v-model="emp.posId" placeholder="position" size="mini" style="width: 150px;">
                                    <el-option
                                            v-for="item in positions"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="jobLevel:" prop="jobLevelId">
                                <el-select v-model="emp.jobLevelId" placeholder="jobLevel" size="mini" style="width: 150px;">
                                    <el-option
                                            v-for="item in joblevels"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="department:" prop="departmentId">
                                <el-popover
                                        placement="right"
                                        title="choose department"
                                        width="200"
                                        trigger="manual"
                                        v-model="popVisible">
                                    <el-tree default-expand-all :data="allDeps" :props="defaultProps"
                                             @node-click="handleNodeClick"></el-tree>
                                    <div slot="reference"
                                         style="width: 150px;display: inline-flex;font-size: 13px;border: 1px solid #dedede;height: 26px;border-radius: 5px;cursor: pointer;align-items: center;padding-left: 8px;box-sizing: border-box"
                                         @click="showDepView">{{inputDepName}}
                                    </div>
                                </el-popover>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="phone:" prop="phone">
                                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-phone"
                                          v-model="emp.phone" placeholder="phone"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="workID:" prop="workID">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="emp.workID" placeholder="workID" disabled></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="degree:" prop="tiptopDegree">
                                <el-select v-model="emp.tiptopDegree" placeholder="degree" size="mini"
                                           style="width: 150px;">
                                    <el-option
                                            v-for="item in tiptopDegrees"
                                            :key="item"
                                            :label="item"
                                            :value="item">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="school:" prop="school">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="emp.school" placeholder="school"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="major:" prop="specialty">
                                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                                          v-model="emp.specialty" placeholder="major"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="beginDate:" prop="beginDate">
                                <el-date-picker
                                        v-model="emp.beginDate"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 130px;">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="conversionTime:" prop="conversionTime">
                                <el-date-picker
                                        v-model="emp.conversionTime"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 130px;">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="beginContract:" prop="beginContract">
                                <el-date-picker
                                        v-model="emp.beginContract"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 130px;">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="endContract:" prop="endContract">
                                <el-date-picker
                                        v-model="emp.endContract"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 150px;">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="idCard:" prop="idCard">
                                <el-input size="mini" style="width: 180px" prefix-icon="el-icon-edit"
                                          v-model="emp.idCard" placeholder="idCard"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="engageForm:" prop="engageForm">
                                <el-radio-group v-model="emp.engageForm">
                                    <el-radio label="劳动合同">employee contract</el-radio>
                                    <el-radio label="劳务合同">labor contract</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="wedlock:" prop="wedlock">
                                <el-radio-group v-model="emp.wedlock">
                                    <el-radio label="已婚">married</el-radio>
                                    <el-radio label="未婚">single</el-radio>
                                    <el-radio label="离异">divorce</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">cancel</el-button>
                <el-button type="primary" @click="doAddEmp">confirm</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "EmpBasic",
        data() {
            return {
                searchValue: {
                    politicId: null,
                    nationId: null,
                    jobLevelId: null,
                    posId: null,
                    engageForm: null,
                    departmentId: null,
                    beginDateScope: null
                },
                title: '',
                importDataBtnText: 'import data',
                importDataBtnIcon: 'el-icon-upload2',
                importDataDisabled: false,
                showAdvanceSearchView: false,
                allDeps: [],
                emps: [],
                loading: false,
                popVisible: false,
                popVisible2: false,
                dialogVisible: false,
                total: 0,
                page: 1,
                keyword: '',
                size: 10,
                nations: [],
                joblevels: [],
                politicsstatus: [],
                positions: [],
                tiptopDegrees: ['大学', '大专', '硕士', '博士', '高中', '初中', '小学', '其他'],
                options: [],
                inputDepName: 'department',
                emp: {
                    name: "javaboy",
                    gender: "男",
                    birthday: "1989-12-31",
                    idCard: "610122199001011256",
                    wedlock: "已婚",
                    nationId: 1,
                    nativePlace: "陕西",
                    politicId: 13,
                    email: "laowang@qq.com",
                    phone: "18565558897",
                    address: "深圳市南山区",
                    departmentId: null,
                    jobLevelId: 9,
                    posId: 29,
                    engageForm: "劳务合同",
                    tiptopDegree: "本科",
                    specialty: "信息管理与信息系统",
                    school: "深圳大学",
                    beginDate: "2017-12-31",
                    workState: "在职",
                    workID: "00000057",
                    contractTerm: 2,
                    conversionTime: "2018-03-31",
                    notworkDate: null,
                    beginContract: "2017-12-31",
                    endContract: "2019-12-31",
                    workAge: null
                },
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                rules: {
                    name: [{required: true, message: 'type name', trigger: 'blur'}],
                    gender: [{required: true, message: 'type gender', trigger: 'blur'}],
                    birthday: [{required: true, message: 'type birthday', trigger: 'blur'}],
                    idCard: [{required: true, message: 'type ID', trigger: 'blur'}, {
                        pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
                        message: 'ID format is incorrect',
                        trigger: 'blur'
                    }],
                    wedlock: [{required: true, message: 'type marriage status', trigger: 'blur'}],
                    nationId: [{required: true, message: 'type nation', trigger: 'blur'}],
                    nativePlace: [{required: true, message: 'type nativePlace', trigger: 'blur'}],
                    politicId: [{required: true, message: 'type politic status', trigger: 'blur'}],
                    email: [{required: true, message: 'type email', trigger: 'blur'}, {
                        type: 'email',
                        message: 'email format incorrect',
                        trigger: 'blur'
                    }],
                    phone: [{required: true, message: 'type phone', trigger: 'blur'}],
                    address: [{required: true, message: 'type address', trigger: 'blur'}],
                    departmentId: [{required: true, message: 'type department', trigger: 'blur'}],
                    jobLevelId: [{required: true, message: 'type jobLevel', trigger: 'blur'}],
                    posId: [{required: true, message: 'type position', trigger: 'blur'}],
                    engageForm: [{required: true, message: 'type contract form', trigger: 'blur'}],
                    tiptopDegree: [{required: true, message: 'type degree', trigger: 'blur'}],
                    specialty: [{required: true, message: 'type major', trigger: 'blur'}],
                    school: [{required: true, message: 'type school', trigger: 'blur'}],
                    beginDate: [{required: true, message: 'type beginDate', trigger: 'blur'}],
                    workState: [{required: true, message: 'type workState', trigger: 'blur'}],
                    workID: [{required: true, message: 'type workID', trigger: 'blur'}],
                    contractTerm: [{required: true, message: 'type contractTerm', trigger: 'blur'}],
                    conversionTime: [{required: true, message: 'type conversionTime', trigger: 'blur'}],
                    notworkDate: [{required: true, message: 'type leaveDate', trigger: 'blur'}],
                    beginContract: [{required: true, message: 'type beginContract', trigger: 'blur'}],
                    endContract: [{required: true, message: 'type endContract', trigger: 'blur'}],
                    workAge: [{required: true, message: 'type workAge', trigger: 'blur'}],
                }
            }
        },
        mounted() {
            this.initEmps();
            this.initData();
            this.initPositions();
        },
        methods: {
            searchViewHandleNodeClick(data) {
                this.inputDepName = data.name;
                this.searchValue.departmentId = data.id;
                this.popVisible2 = !this.popVisible2
            },
            onError(err, file, fileList) {
                this.importDataBtnText = 'load data';
                this.importDataBtnIcon = 'el-icon-upload2';
                this.importDataDisabled = false;
            },
            onSuccess(response, file, fileList) {
                this.importDataBtnText = 'load date';
                this.importDataBtnIcon = 'el-icon-upload2';
                this.importDataDisabled = false;
                this.initEmps();
            },
            beforeUpload() {
                this.importDataBtnText = 'loading';
                this.importDataBtnIcon = 'el-icon-loading';
                this.importDataDisabled = true;
            },
            exportData() {
                window.open('/employee/basic/export', '_parent');
            },
            emptyEmp() {
                this.emp = {
                    name: "",
                    gender: "",
                    birthday: "",
                    idCard: "",
                    wedlock: "",
                    nationId: 1,
                    nativePlace: "",
                    politicId: 13,
                    email: "",
                    phone: "",
                    address: "",
                    departmentId: null,
                    jobLevelId: 9,
                    posId: 29,
                    engageForm: "",
                    tiptopDegree: "",
                    specialty: "",
                    school: "",
                    beginDate: "",
                    workID: "",
                    contractTerm: 2,
                    conversionTime: "",
                    notworkDate: null,
                    beginContract: "",
                    endContract: "",
                    workAge: null
                }
                this.inputDepName = '';
            },
            showEditEmpView(data) {
                this.initPositions();
                this.title = 'edit employee info';
                this.emp = data;
                this.inputDepName = data.department.name;
                this.dialogVisible = true;
            },
            deleteEmp(data) {
                this.$confirm('this operation will permanently delete [' + data.name + ']', 'prompt', {
                    confirmButtonText: 'confirm',
                    cancelButtonText: 'cancel',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/employee/basic/" + data.id).then(resp => {
                        if (resp) {
                            this.initEmps();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: 'delete canceled'
                    });
                });
            },
            doAddEmp() {
                if (this.emp.id) {
                    this.$refs['empForm'].validate(valid => {
                        if (valid) {
                            this.putRequest("/employee/basic/", this.emp).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEmps();
                                }
                            })
                        }
                    });
                } else {
                    this.$refs['empForm'].validate(valid => {
                        if (valid) {
                            this.postRequest("/employee/basic/", this.emp).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEmps();
                                }
                            })
                        }
                    });
                }
            },
            handleNodeClick(data) {
                this.inputDepName = data.name;
                this.emp.departmentId = data.id;
                this.popVisible = !this.popVisible
            },
            showDepView() {
                this.popVisible = !this.popVisible
            },
            showDepView2() {
                this.popVisible2 = !this.popVisible2
            },
            initPositions() {
                this.getRequest('/employee/basic/positions').then(resp => {
                    if (resp) {
                        this.positions = resp;
                    }
                })
            },
            getMaxWordID() {
                this.getRequest("/employee/basic/maxWorkID").then(resp => {
                    if (resp) {
                        this.emp.workID = resp.obj;
                    }
                })
            },
            initData() {
                if (!window.sessionStorage.getItem("nations")) {
                    this.getRequest('/employee/basic/nations').then(resp => {
                        if (resp) {
                            this.nations = resp;
                            window.sessionStorage.setItem("nations", JSON.stringify(resp));
                        }
                    })
                } else {
                    this.nations = JSON.parse(window.sessionStorage.getItem("nations"));
                }
                if (!window.sessionStorage.getItem("joblevels")) {
                    this.getRequest('/employee/basic/joblevels').then(resp => {
                        if (resp) {
                            this.joblevels = resp;
                            window.sessionStorage.setItem("joblevels", JSON.stringify(resp));
                        }
                    })
                } else {
                    this.joblevels = JSON.parse(window.sessionStorage.getItem("joblevels"));
                }
                if (!window.sessionStorage.getItem("politicsstatus")) {
                    this.getRequest('/employee/basic/politicsstatus').then(resp => {
                        if (resp) {
                            this.politicsstatus = resp;
                            window.sessionStorage.setItem("politicsstatus", JSON.stringify(resp));
                        }
                    })
                } else {
                    this.politicsstatus = JSON.parse(window.sessionStorage.getItem("politicsstatus"));
                }
                if (!window.sessionStorage.getItem("deps")) {
                    this.getRequest('/employee/basic/deps').then(resp => {
                        if (resp) {
                            this.allDeps = resp;
                            window.sessionStorage.setItem("deps", JSON.stringify(resp));
                        }
                    })
                } else {
                    this.allDeps = JSON.parse(window.sessionStorage.getItem("deps"));
                }
            },
            sizeChange(currentSize) {
                this.size = currentSize;
                this.initEmps();
            },
            currentChange(currentPage) {
                this.page = currentPage;
                this.initEmps('advanced');
            },
            showAddEmpView() {
                this.emptyEmp();
                this.title = 'add employee';
                this.getMaxWordID();
                this.dialogVisible = true;
            },
            initEmps(type) {
                this.loading = true;
                let url = '/employee/basic/?page=' + this.page + '&size=' + this.size;
                if (type && type == 'advanced') {
                    if (this.searchValue.politicId) {
                        url += '&politicId=' + this.searchValue.politicId;
                    }
                    if (this.searchValue.nationId) {
                        url += '&nationId=' + this.searchValue.nationId;
                    }
                    if (this.searchValue.jobLevelId) {
                        url += '&jobLevelId=' + this.searchValue.jobLevelId;
                    }
                    if (this.searchValue.posId) {
                        url += '&posId=' + this.searchValue.posId;
                    }
                    if (this.searchValue.engageForm) {
                        url += '&engageForm=' + this.searchValue.engageForm;
                    }
                    if (this.searchValue.departmentId) {
                        url += '&departmentId=' + this.searchValue.departmentId;
                    }
                    if (this.searchValue.beginDateScope) {
                        url += '&beginDateScope=' + this.searchValue.beginDateScope;
                    }
                } else {
                    url += "&name=" + this.keyword;
                }
                this.getRequest(url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.emps = resp.data;
                        this.total = resp.total;
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>