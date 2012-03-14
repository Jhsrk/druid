package com.alibaba.druid.filter.wall.spi;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.filter.wall.IllegalStatementViolation;
import com.alibaba.druid.filter.wall.Violation;
import com.alibaba.druid.filter.wall.WallVisitor;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExecuteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowColumnsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDatabasesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTablesStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

public class MySqlWallVisitor extends MySqlASTVisitorAdapter implements WallVisitor {

    private final List<Violation> violations;

    public MySqlWallVisitor() {
        this(new ArrayList<Violation>());
    }

    public MySqlWallVisitor(List<Violation> violations) {
        this.violations = violations;
    }

    public List<Violation> getViolations() {
        return violations;
    }

    public boolean visit(MySqlExecuteStatement x) {
        violations.add(new IllegalStatementViolation(SQLUtils.toMySqlString(x)));
        return false;
    }
    
    @Override
    public boolean visit(MySqlShowTablesStatement x) {
        violations.add(new IllegalStatementViolation(SQLUtils.toMySqlString(x)));
        return false;
    }
    
    @Override
    public boolean visit(MySqlShowDatabasesStatement x) {
        violations.add(new IllegalStatementViolation(SQLUtils.toMySqlString(x)));
        return false;
    }
    
    @Override
    public boolean visit(MySqlShowColumnsStatement x) {
        violations.add(new IllegalStatementViolation(SQLUtils.toMySqlString(x)));
        return false;
    }
    
    public boolean visit(SQLBinaryOpExpr x) {
        WallVisitorUtils.check(this, x);

        return true;
    }
    
    @Override
    public boolean visit(SQLSelectQueryBlock x) {
        if (x.getWhere() != null) {
            x.getWhere().setParent(x);
        }
        
        return true;
    }
    
    @Override
    public boolean visit(MySqlSelectQueryBlock x) {
        return visit((SQLSelectQueryBlock) x);
    }
}