<template>
    <div>
        <div>
            <el-input
                    size="small"
                    class="addPosInput"
                    v-loading="loading"
                    element-loading-text="loading..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    placeholder="adding position..."
                    prefix-icon="el-icon-plus"
                    @keydown.enter.native="addPosition"
                    v-model="pos.name">
            </el-input>
            <el-button icon="el-icon-plus" size="small" type="primary" @click="addPosition">add</el-button>
        </div>
        <div class="posManaMain">
            <el-table
                    :data="positions"
                    border
                    @selection-change="handleSelectionChange"
                    size="small"
                    stripe
                    style="width: 70%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="id"
                        label="id"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="name"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        width="150"
                        label="createDate">
                </el-table-column>
                <el-table-column
                        label="enabled">
                    <template slot-scope="scope">
                        <el-tag size="small" type="success" v-if="scope.row.enabled">enabled</el-tag>
                        <el-tag size="small" type="danger" v-else>disabled</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="operation">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="showEditView(scope.$index, scope.row)">edit
                        </el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, scope.row)">delete
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-button @click="deleteMany" type="danger" size="small" style="margin-top: 8px"
                       :disabled="multipleSelection.length==0">delete by batch
            </el-button>
        </div>
        <el-dialog
                title="edit position"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <div>
                    <el-tag>position name</el-tag>
                    <el-input class="updatePosInput" size="small" v-model="updatePos.name"></el-input>
                </div>
                <div>
                    <el-tag>enabled</el-tag>
                    <el-switch
                            v-model="updatePos.enabled"
                            active-text="enable"
                            inactive-text="disable">
                    </el-switch>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
    <el-button size="small" @click="dialogVisible = false">cancel</el-button>
    <el-button size="small" type="primary" @click="doUpdate">confirm</el-button>
  </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "PosMana",
        data() {
            return {
                pos: {
                    name: ''
                },
                dialogVisible: false,
                loading: false,
                updatePos: {
                    name: '',
                    enabled: false
                },
                multipleSelection: [],
                positions: []
            }
        },
        mounted() {
            this.initPositions();
        },
        methods: {
            deleteMany() {
                this.$confirm('this operation will permanently delete [' + this.multipleSelection.length + '] records', 'prompt', {
                    confirmButtonText: 'confirm',
                    cancelButtonText: 'cancel',
                    type: 'warning'
                }).then(() => {
                    let ids = '?';
                    this.multipleSelection.forEach(item => {
                        ids += 'ids=' + item.id + '&';
                    })
                    this.deleteRequest("/system/basic/pos/" + ids).then(resp => {
                        if (resp) {
                            this.initPositions();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: 'delete canceled'
                    });
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            addPosition() {
                if (this.pos.name) {
                    this.postRequest("/system/basic/pos/", this.pos).then(resp => {
                        if (resp) {
                            this.initPositions();
                            this.pos.name = '';
                        }
                    })
                } else {
                    this.$message.error('position name can not be null');
                }
            },
            showEditView(index, data) {
                Object.assign(this.updatePos, data);
                this.dialogVisible = true;
            },
            doUpdate() {
                this.putRequest("/system/basic/pos/", this.updatePos).then(resp => {
                    if (resp) {
                        this.initPositions();
                        this.updatePos.name = '';
                        this.dialogVisible = false;
                    }
                })
            },
            handleDelete(index, data) {
                this.$confirm('this operation will permanently delete [' + data.name + '] record', 'prompt', {
                    confirmButtonText: 'confirm',
                    cancelButtonText: 'cancel',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/system/basic/pos/" + data.id).then(resp => {
                        if (resp) {
                            this.initPositions();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: 'delete canceled'
                    });
                });
            },
            initPositions() {
                this.loading = true;
                this.getRequest("/system/basic/pos/").then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.positions = resp;
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .addPosInput {
        width: 300px;
        margin-right: 8px
    }

    .updatePosInput {
        width: 200px;
        margin-left: 8px;
    }

    .posManaMain {
        margin-top: 10px;
    }
</style>